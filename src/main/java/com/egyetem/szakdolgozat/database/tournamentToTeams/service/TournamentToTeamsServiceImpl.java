package com.egyetem.szakdolgozat.database.tournamentToTeams.service;

import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeams;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeamsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TournamentToTeamsServiceImpl implements TournamentToTeamsService{

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
}
