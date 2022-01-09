package com.egyetem.szakdolgozat.user.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteUserRepository extends JpaRepository<SiteUser, Integer> {

    SiteUser findUserById(Long id);
}
