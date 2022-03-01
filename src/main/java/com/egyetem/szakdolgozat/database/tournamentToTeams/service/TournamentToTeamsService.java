package com.egyetem.szakdolgozat.database.tournamentToTeams.service;

import com.egyetem.szakdolgozat.database.team.persistance.Team;
import com.egyetem.szakdolgozat.database.tournament.persistance.Tournament;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeams;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;

import java.util.Set;

public interface TournamentToTeamsService {

    Set<TournamentToTeams> getTournamentToTeamsById(Long id);

    TournamentToTeams constructNewTournamentToTeams(Long teamId, Long tournamentId);

    boolean validateAndSaveOrDelete(Tournament tournament, TournamentToTeams tournamentToTeams, Team team, SiteUser siteUser, boolean save);

    TournamentToTeams getById(Long teamId, Long tournamentId);

    boolean validateAndEliminateTeam(Tournament tournament, TournamentToTeams tournamentToTeams, SiteUser siteUser, Long eliminationRound);
}
