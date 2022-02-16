package com.egyetem.szakdolgozat.database.tournament.service;

import com.egyetem.szakdolgozat.database.team.persistance.Team;
import com.egyetem.szakdolgozat.database.tournament.persistance.Tournament;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeams;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;
import com.egyetem.szakdolgozat.notify.Notifier;
import com.egyetem.szakdolgozat.util.RoundCalculator;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TournamentServiceImpl implements TournamentService{

    private final TaskExecutor executor;

    @Autowired
    public TournamentServiceImpl(@Qualifier("taskExecutor") TaskExecutor taskExecutor) {
        this.executor = taskExecutor;
    }

    @Override
    public void emailSenderService(Tournament tournament) {

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
                Notifier.notifyUsers(tournament);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public List<List<TournamentToTeams>> currentStandingCalculatorService(Tournament tournament) {

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
                firstRound.add(new TournamentToTeams(i, -1, 1));
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
}
