package com.egyetem.szakdolgozat.database.regionalAccount.controller;

import com.egyetem.szakdolgozat.database.regionalAccount.persistance.RegionalAccount;
import com.egyetem.szakdolgozat.database.regionalAccount.persistance.RegionalAccountRepository;
import com.egyetem.szakdolgozat.database.user.controller.SiteUserController;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;


@RestController
public class RegionalAccountController {

    RegionalAccountRepository regionalAccountRepository;
    SiteUserRepository siteUserRepository;

    @Autowired
    public RegionalAccountController(RegionalAccountRepository regionalAccountRepository, SiteUserRepository siteUserRepository) {
        this.regionalAccountRepository = regionalAccountRepository;
        this.siteUserRepository = siteUserRepository;
    }

    @GetMapping(value = "/api/regionalAccounts/{userId}/", produces = "application/json")
    public ResponseEntity<Object> getAll(@PathVariable Long userId) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            SiteUser siteUser = siteUserRepository.findUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            System.out.println(authentication.getName());
            System.out.println(siteUser.getUsername());

            if (!(authentication.getName().equals(siteUser.getUsername()))){
                return new ResponseEntity<>("\"Error, that's not you.\"", HttpStatus.FORBIDDEN);
            }

            Set<RegionalAccount> regionalAccounts = regionalAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("\"User not found.\""));

            return new ResponseEntity<>(regionalAccounts, HttpStatus.OK);
        }catch (ResourceNotFoundException e){
            return new ResponseEntity<>("\"Error: " +e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/api/regionalAccounts/update", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> updateRegionalAccountInfo(@RequestBody RegionalAccount regionalAccount) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            SiteUser siteUser = siteUserRepository.findUserById(regionalAccount.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            if (!(authentication.getName().equals(siteUser.getUsername()))){
                return new ResponseEntity<>("\"Error, that's not you.\"", HttpStatus.FORBIDDEN);
            }

            if (!(regionalAccount.getRegionId().isBlank() || regionalAccount.getInGameName().isBlank())) {
                regionalAccountRepository.save(regionalAccount);
                return new ResponseEntity<>("\"Change saved.\"", HttpStatus.OK);
            }
            return new ResponseEntity<>("\"Error, change not saved. No field can be blank.\"", HttpStatus.BAD_REQUEST);
        }catch (ResourceNotFoundException e){
            return new ResponseEntity<>("\"Error: " +e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/api/regionalAccounts/delete", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> deleteRegionalAccount(@RequestBody RegionalAccount regionalAccount) {
        try {
            SiteUser siteUser = siteUserRepository.findUserById(regionalAccount.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (!(authentication.getName().equals(siteUser.getUsername()))){
                return new ResponseEntity<>("\"Error, that's not you.\"", HttpStatus.FORBIDDEN);
            }

            regionalAccountRepository.delete(regionalAccount);
            return new ResponseEntity<>("\"Entity deleted.\"", HttpStatus.OK);

        } catch (ResourceNotFoundException e){
            return new ResponseEntity<>("\"Error: " +e.getMessage() + "\"", HttpStatus.NOT_FOUND);

        } catch (RuntimeException e) {
            return new ResponseEntity<>("\"An unknown error has occoured, check request fields.\"",
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
