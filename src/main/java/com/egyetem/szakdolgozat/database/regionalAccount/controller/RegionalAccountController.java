package com.egyetem.szakdolgozat.database.regionalAccount.controller;

import com.egyetem.szakdolgozat.database.regionalAccount.persistance.RegionalAccount;
import com.egyetem.szakdolgozat.database.regionalAccount.service.RegionalAccountService;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;
import com.egyetem.szakdolgozat.database.user.service.SiteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;


@RestController
public class RegionalAccountController {

    RegionalAccountService regionalAccountService;
    SiteUserService siteUserService;

    @Autowired
    public RegionalAccountController(RegionalAccountService regionalAccountService,SiteUserService siteUserService) {
        this.regionalAccountService = regionalAccountService;
        this.siteUserService = siteUserService;
    }

    @GetMapping(value = "/api/regionalAccounts/", produces = "application/json")
    public ResponseEntity<Object> getAll() {
        try {
            SiteUser siteUser = siteUserService.getCurrentlyLoggedInSiteUser();
            Set<RegionalAccount> regionalAccounts = regionalAccountService.getRegionalAccountsFromId(siteUser.getId());

            return new ResponseEntity<>(regionalAccounts, HttpStatus.OK);
        }catch (ResourceNotFoundException e){
            return new ResponseEntity<>("\"Error: " +e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/api/regionalAccounts/update", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> updateRegionalAccountInfo(@RequestBody RegionalAccount regionalAccount) {
        try {
            SiteUser siteUser = siteUserService.getCurrentlyLoggedInSiteUser();

            if (regionalAccountService.validateAndSave(regionalAccount, siteUser)){
                return new ResponseEntity<>("\"Change saved.\"", HttpStatus.OK);
            }
            return new ResponseEntity<>("\"Error, change not saved. No field can be blank.\"", HttpStatus.BAD_REQUEST);
        }catch (ResourceNotFoundException e){
            return new ResponseEntity<>("\"Error: " +e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/api/regionalAccounts/delete", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> deleteRegionalAccount(@RequestBody RegionalAccount regionalAccount) {
        try {
            SiteUser siteUser = siteUserService.getCurrentlyLoggedInSiteUser();
            regionalAccountService.deleteAndValidate(regionalAccount, siteUser);

            return new ResponseEntity<>("\"Entity deleted.\"", HttpStatus.OK);
        } catch (ResourceNotFoundException e){
            return new ResponseEntity<>("\"Error: " +e.getMessage() + "\"", HttpStatus.NOT_FOUND);

        } catch (RuntimeException e) {
            return new ResponseEntity<>("\"An unknown error has occoured, check request fields.\"",
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
