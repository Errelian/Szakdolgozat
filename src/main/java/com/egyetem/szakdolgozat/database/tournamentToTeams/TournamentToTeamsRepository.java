package com.egyetem.szakdolgozat.database.tournamentToTeams;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TournamentToTeamsRepository extends JpaRepository<TournamentToTeams, Integer> {

    Optional<TournamentToTeams> findTournamentToTeamsById(TournamentToTeamsCKey tournamentToTeamsCKey);
}
