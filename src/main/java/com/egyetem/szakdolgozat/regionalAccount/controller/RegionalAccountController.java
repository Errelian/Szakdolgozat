package com.egyetem.szakdolgozat.regionalAccount.controller;

import com.egyetem.szakdolgozat.region.persistance.Region;
import com.egyetem.szakdolgozat.regionalAccount.persistance.RegionalAccount;
import com.egyetem.szakdolgozat.regionalAccount.persistance.RegionalAccountRepository;
import com.egyetem.szakdolgozat.team.persistance.TeamRepository;
import com.egyetem.szakdolgozat.user.persistance.SiteUser;
import com.egyetem.szakdolgozat.user.persistance.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class RegionalAccountController {

    RegionalAccountRepository regionalAccountRepository;

    @Autowired
    public RegionalAccountController(RegionalAccountRepository regionalAccountRepository) {
        this.regionalAccountRepository = regionalAccountRepository;
    }

    @GetMapping(value = "/regionalAccounts/{userId}/")
    public Set<RegionalAccount> getAll(@PathVariable Long userId) {
        return regionalAccountRepository.findByUserId(userId);
    }

    @PostMapping(value = "/regionalAccounts/update", consumes = "application/json")
    public String updateRegionalAccountInfo(@RequestBody RegionalAccount regionalAccount){
        if (!(regionalAccount.getRegionId().isBlank() || regionalAccount.getInGameName().isBlank())){
            regionalAccountRepository.save(regionalAccount);
            return "Change saved.";
        }
        return "Error, change no saved. No field can be blank.";
    }

    @DeleteMapping(value = "/regionalAccounts/delete", consumes = "application/json")
    public String deleteRegionalAccount(@RequestBody RegionalAccount regionalAccount){
        regionalAccountRepository.delete(regionalAccount);
        return "If the entity existed, it was deleted.";
    }
}
