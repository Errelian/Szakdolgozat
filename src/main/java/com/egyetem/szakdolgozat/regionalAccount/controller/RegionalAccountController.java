package com.egyetem.szakdolgozat.regionalAccount.controller;

import com.egyetem.szakdolgozat.regionalAccount.persistance.RegionalAccount;
import com.egyetem.szakdolgozat.regionalAccount.persistance.RegionalAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    public RegionalAccountController(RegionalAccountRepository regionalAccountRepository) {
        this.regionalAccountRepository = regionalAccountRepository;
    }

    @GetMapping(value = "/api/regionalAccounts/{userId}/", produces = "application/json")
    public ResponseEntity<Object> getAll(@PathVariable Long userId) {
        try {
            Set<RegionalAccount> regionalAccounts = regionalAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("\"User not found.\""));

            return new ResponseEntity<>(regionalAccounts, HttpStatus.OK);
        }catch (ResourceNotFoundException e){
            return new ResponseEntity<>("\"Error: " +e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/api/regionalAccounts/update", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> updateRegionalAccountInfo(@RequestBody RegionalAccount regionalAccount) {
        if (!(regionalAccount.getRegionId().isBlank() || regionalAccount.getInGameName().isBlank())) {
            regionalAccountRepository.save(regionalAccount);
            return new ResponseEntity<>("\"Change saved.\"", HttpStatus.OK);
        }
        return new ResponseEntity<>("\"Error, change no saved. No field can be blank.\"", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/regionalAccounts/delete", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> deleteRegionalAccount(@RequestBody RegionalAccount regionalAccount) {
        try {
            regionalAccountRepository.delete(regionalAccount);
            return new ResponseEntity<>("\"Entity deleted.\"", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("\"An unknown error has occoured, check request fields.\"",
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
