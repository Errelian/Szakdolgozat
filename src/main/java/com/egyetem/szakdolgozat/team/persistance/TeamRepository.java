package com.egyetem.szakdolgozat.team.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {

    //@Query("select team from Team team inner join team.teamMembers member where member.id = ?1")
    //Set<Team> findTeamsByQuery(Long userId);


    Set<Team> findByTeamMembers_Id(Long id);
}
