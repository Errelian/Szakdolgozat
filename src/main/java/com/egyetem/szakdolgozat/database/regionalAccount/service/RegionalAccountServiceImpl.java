package com.egyetem.szakdolgozat.database.regionalAccount.service;

import com.egyetem.szakdolgozat.database.regionalAccount.persistance.RegionalAccount;
import com.egyetem.szakdolgozat.database.regionalAccount.persistance.RegionalAccountRepository;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> getRegionalAccountsFromId(Long id) {
        Set<RegionalAccount> regionalAccounts = regionalAccountRepository.findByUserId(id)
            .orElseThrow(() -> new ResourceNotFoundException("\"User not found.\""));

        return new ResponseEntity<>(regionalAccounts, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> validateAndSave(RegionalAccount regionalAccount, SiteUser siteUser) {
        if (!(regionalAccount.getRegionId().isBlank() || regionalAccount.getInGameName().isBlank())) {
            regionalAccount.setUserId(siteUser.getId());
            regionalAccountRepository.save(regionalAccount);
            return new ResponseEntity<>("\"Change saved.\"", HttpStatus.OK);
        }
        return new ResponseEntity<>("\"Error, change not saved. No field can be blank.\"", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> deleteAndValidate(RegionalAccount regionalAccount, SiteUser siteUser) {
        regionalAccount.setUserId(siteUser.getId());

        regionalAccountRepository.delete(regionalAccount);
        return new ResponseEntity<>("\"Entity deleted.\"", HttpStatus.OK);
    }
}
