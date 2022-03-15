package com.egyetem.szakdolgozat.database.tournament.service;

import com.egyetem.szakdolgozat.database.team.persistance.Team;
import com.egyetem.szakdolgozat.database.tournament.persistance.Tournament;
import com.egyetem.szakdolgozat.database.tournament.persistance.TournamentRepository;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeams;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;
import com.egyetem.szakdolgozat.notify.NotificationServiceImpl;
import com.egyetem.szakdolgozat.util.RoundCalculator;
import com.egyetem.szakdolgozat.util.UnauthorizedException;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TournamentServiceImpl implements TournamentService{

    private final TaskExecutor executor;
    private final TournamentRepository tournamentRepository;

    @Autowired
    public TournamentServiceImpl(@Qualifier("taskExecutor") TaskExecutor taskExecutor, TournamentRepository tournamentRepository) {
        this.executor = taskExecutor;
        this.tournamentRepository = tournamentRepository;
    }

    @Override
    public void emailSenderService(Tournament tournament) {
        cacheEvictor();
        List<Team> teams = new ArrayList<>();
        for (TournamentToTeams tournamentToTeam : tournament.getTeams()) {
            teams.add(tournamentToTeam.getTeam());
        }
        for (Team team : teams) {
            for (SiteUser user : team.getTeamMembers()){
                user.geteMail();
            }
        }

        executor.execute(() -> {
            try {
                NotificationServiceImpl.notifyUsers(tournament);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        });
    }

    @CacheEvict(value="tournaments",allEntries = true)
    public void cacheEvictor(){
        System.out.println("Cache evicted.");
    }

    @Override

    public List<List<TournamentToTeams>> currentStandingCalculatorService(Tournament tournament) {
        cacheEvictor();
        List<TournamentToTeams> teamList = tournament.getTeams();
        List<TournamentToTeams> firstRound = new ArrayList<>();
        int rounds = (int) Math.ceil(Math.log(teamList.size()) / Math.log(2));
        int maxTeams = (int) Math.pow(2, rounds);


        for (int i = 1; i <= maxTeams; i++){
            boolean match = false;
            for (TournamentToTeams team : teamList){
                if (team.getPosition() == i){
                    match = true;
                    firstRound.add(team);
                }
            }
            if(!match){
                firstRound.add(new TournamentToTeams(i, -1, 1, new Team(-1L, "Fake team")));
            }
        }

        firstRound.sort(Comparator.comparing(TournamentToTeams::getPosition));

        List<List<TournamentToTeams>> currentTourneyStanding = new ArrayList<>();


        for (int i = 0; i <= (int) (Math.log(firstRound.size())/Math.log(2)); i++){
            if (i == 0){
                currentTourneyStanding.add(firstRound);
            }
            else{
                currentTourneyStanding.add(
                    RoundCalculator.calcNextRound2(currentTourneyStanding.get(currentTourneyStanding.size()-1), i+1));
            }
        }

        return currentTourneyStanding;
    }

    @Override
    public void setCreatorAndSave(Tournament tournament, SiteUser siteUser) {
        tournament.setCreatorId(siteUser.getId());
        tournament.setUpdating(false);
        tournamentRepository.save(tournament);
    }

    @Override
    @Cacheable(value = "tournaments", key="#id")
    public Tournament getById(Long id) {
        return tournamentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tournament not found."));
    }

    @Override
    @CacheEvict(value = "tournaments", key = "#tournament.id")
    public void validateAndDelete(Tournament tournament, SiteUser siteUser) throws UnauthorizedException {
        if(tournament.getCreatorId().equals(siteUser.getId())){
            tournamentRepository.deleteById(tournament.getId());
        }
        throw new UnauthorizedException();
    }

    @Override
    @CachePut(value = "tournaments", key="#tournament.id")
    public Tournament validateAndSaveNewName(Tournament tournament, SiteUser siteUser, String newName)
        throws UnauthorizedException {
        if (siteUser.getId().equals(tournament.getCreatorId())){
            tournament.setTournamentName(newName);
            return tournamentRepository.save(tournament);

        }
        throw new UnauthorizedException();
    }

    @Override
    @CachePut(value = "tournaments", key="#tournament.id")
    public Tournament validateAndSaveVictor(Tournament tournament, SiteUser siteUser, Long victor)
        throws UnauthorizedException {
        if (siteUser.getId().equals(tournament.getCreatorId())){
            tournament.setVictorId(victor);
            return tournamentRepository.save(tournament);
        }
        throw new UnauthorizedException();
    }

    @Override
    @CachePut(value = "tournaments", key="#tournament.id")
    public Tournament validateAndChangeRegion(Tournament tournament, SiteUser siteUser, String region)
        throws UnauthorizedException {
        if (siteUser.getId().equals(tournament.getCreatorId())) {
            tournament.setRegionId(region);
            return tournamentRepository.save(tournament);
        }
        throw new UnauthorizedException();
    }

    @Override
    public Tournament getByName(String name) {
        return tournamentRepository.findByTournamentName(name).orElseThrow(() -> new ResourceNotFoundException("Tournament not found."));
    }

    @Override
    public List<Tournament> getAll() {
        return tournamentRepository.findAll();
    }

    @Override
    public boolean validate(Tournament tournament) {
        return !tournament.getTournamentName().isBlank() &&
            !tournament.getRegionId().isBlank() && !tournament.getStartTime().toString().isBlank();
    }
}
