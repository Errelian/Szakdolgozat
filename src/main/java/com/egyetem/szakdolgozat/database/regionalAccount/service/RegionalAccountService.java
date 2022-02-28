package com.egyetem.szakdolgozat.database.regionalAccount.service;

import com.egyetem.szakdolgozat.database.regionalAccount.persistance.RegionalAccount;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;

import java.util.Set;

public interface RegionalAccountService {

    Set<RegionalAccount> getRegionalAccountsFromId(Long id);

    boolean validateAndSave(RegionalAccount regionalAccount, SiteUser siteUser);

    void deleteAndValidate(RegionalAccount regionalAccount, SiteUser siteUser);
}
