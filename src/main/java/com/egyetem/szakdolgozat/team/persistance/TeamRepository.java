package com.egyetem.szakdolgozat.team.persistance;

import com.egyetem.szakdolgozat.user.persistance.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
}
