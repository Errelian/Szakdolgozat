package com.egyetem.szakdolgozat.regionalAccount.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface RegionalAccountRepository  extends JpaRepository<RegionalAccount, Long> {


    Optional<Set<RegionalAccount>> findByUserId(Long userId);
}
