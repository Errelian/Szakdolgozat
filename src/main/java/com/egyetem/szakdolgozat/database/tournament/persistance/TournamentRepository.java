package com.egyetem.szakdolgozat.database.tournament.persistance;

import com.egyetem.szakdolgozat.database.team.persistance.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Integer> {

    @Transactional
    void deleteById(Long id);

    Optional<Tournament> findByTournamentName(String tournamentName);

    Optional<Tournament> findById(Long id);
}
