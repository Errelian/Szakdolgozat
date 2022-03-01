package com.egyetem.szakdolgozat.database.tournamentToTeams.service;

import com.egyetem.szakdolgozat.database.team.persistance.Team;
import com.egyetem.szakdolgozat.database.tournament.persistance.Tournament;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeams;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeamsCKey;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeamsRepository;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TournamentToTeamsServiceImpl implements TournamentToTeamsService {

    TournamentToTeamsRepository tournamentToTeamsRepository;

    @Autowired
    public TournamentToTeamsServiceImpl(TournamentToTeamsRepository tournamentToTeamsRepository) {
        this.tournamentToTeamsRepository = tournamentToTeamsRepository;
    }

    @Override
    public Set<TournamentToTeams> getTournamentToTeamsById(Long id) {
        return tournamentToTeamsRepository.findTournamentToTeamsByTeamId(id)
            .orElseThrow(() -> new ResourceNotFoundException("Team not found."));
    }

    @Override
    public TournamentToTeams constructNewTournamentToTeams(Long teamId, Long tournamentId) {
        return new TournamentToTeams(new TournamentToTeamsCKey(teamId, tournamentId));
    }

    @Override
    public boolean validateAndSaveOrDelete(Tournament tournament, TournamentToTeams tournamentToTeams, Team team,
                                           SiteUser siteUser, boolean save) {
        if (siteUser.getId().equals(team.getCreatorId())) {
            tournamentToTeams.setTeam(team);
            tournamentToTeams.setTournament(tournament);

            if (save) {
                tournamentToTeams.setCurrentRound(1);
                tournamentToTeamsRepository.save(tournamentToTeams);
            } else {
                tournamentToTeamsRepository.delete(tournamentToTeams);
            }
            return true;
        }
        return false;
    }

    @Override
    public TournamentToTeams getById(Long teamId, Long tournamentId) {
        return tournamentToTeamsRepository.findTournamentToTeamsById(new TournamentToTeamsCKey(teamId, tournamentId))
            .orElseThrow(() -> new ResourceNotFoundException("Tournament not found."));
    }

    @Override
    public boolean validateAndEliminateTeam(Tournament tournament, TournamentToTeams tournamentToTeams,
                                            SiteUser siteUser, Long eliminationRound) {
        if (siteUser.getId().equals(tournament.getCreatorId())) {
            tournamentToTeams.setEliminationRound(Math.toIntExact(eliminationRound));
            tournamentToTeams.setCurrentRound(tournamentToTeams.getCurrentRound() + 1);
            tournamentToTeamsRepository.save(tournamentToTeams);

            return true;
        }
        return false;
    }

}
