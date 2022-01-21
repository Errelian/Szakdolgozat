package com.egyetem.szakdolgozat.database.user.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SiteUserRepository extends JpaRepository<SiteUser, Integer> {

    Optional<SiteUser> findUserById(Long id);

}
