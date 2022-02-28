package com.egyetem.szakdolgozat.database.regionalAccount.service;

import com.egyetem.szakdolgozat.database.regionalAccount.persistance.RegionalAccount;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;
import org.springframework.http.ResponseEntity;

public interface RegionalAccountService {

    ResponseEntity<Object> getRegionalAccountsFromId(Long id);

    ResponseEntity<Object> validateAndSave(RegionalAccount regionalAccount, SiteUser siteUser);

    ResponseEntity<Object> deleteAndValidate(RegionalAccount regionalAccount, SiteUser siteUser);
}
