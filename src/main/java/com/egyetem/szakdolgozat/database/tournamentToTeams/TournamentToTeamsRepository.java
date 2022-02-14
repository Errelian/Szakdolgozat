package com.egyetem.szakdolgozat.database.tournamentToTeams;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface TournamentToTeamsRepository extends JpaRepository<TournamentToTeams, Integer> {

    Optional<TournamentToTeams> findTournamentToTeamsById(TournamentToTeamsCKey tournamentToTeamsCKey);

    Optional<Set<TournamentToTeams>> findTournamentToTeamsByTeamId(Long id);
}
