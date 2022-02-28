package com.egyetem.szakdolgozat.database.tournamentToTeams.service;

import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeams;

import java.util.Set;

public interface TournamentToTeamsService {

    Set<TournamentToTeams> getTournamentToTeamsById(Long id);
}
