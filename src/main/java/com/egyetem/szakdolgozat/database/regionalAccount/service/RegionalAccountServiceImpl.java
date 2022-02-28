package com.egyetem.szakdolgozat.database.regionalAccount.service;

import com.egyetem.szakdolgozat.database.regionalAccount.persistance.RegionalAccount;
import com.egyetem.szakdolgozat.database.regionalAccount.persistance.RegionalAccountRepository;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RegionalAccountServiceImpl implements RegionalAccountService{

    RegionalAccountRepository regionalAccountRepository;

    @Autowired
    public RegionalAccountServiceImpl(RegionalAccountRepository regionalAccountRepository) {
        this.regionalAccountRepository = regionalAccountRepository;
    }

    @Override
    public Set<RegionalAccount> getRegionalAccountsFromId(Long id) {
        return regionalAccountRepository.findByUserId(id).orElseThrow(() -> new ResourceNotFoundException("\"User not found.\""));
    }

    @Override
    public boolean validateAndSave(RegionalAccount regionalAccount, SiteUser siteUser) {
        if (!(regionalAccount.getRegionId().isBlank() || regionalAccount.getInGameName().isBlank())) {
            regionalAccount.setUserId(siteUser.getId());
            regionalAccountRepository.save(regionalAccount);
            return true;
        }
        return false;
    }

    @Override
    public void deleteAndValidate(RegionalAccount regionalAccount, SiteUser siteUser) {
        regionalAccount.setUserId(siteUser.getId());

        regionalAccountRepository.delete(regionalAccount);
    }
}
