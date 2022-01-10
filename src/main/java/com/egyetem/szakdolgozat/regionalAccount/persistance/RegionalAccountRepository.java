package com.egyetem.szakdolgozat.regionalAccount.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RegionalAccountRepository  extends JpaRepository<RegionalAccount, Long> {


    Set<RegionalAccount> findByUserId(Long userId);
}
