package com.egyetem.szakdolgozat.database.user.controller;

import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUserRegisterer;
import com.egyetem.szakdolgozat.database.user.service.SiteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SiteUserController {

    SiteUserService siteUserService;

    @Autowired
    public SiteUserController(SiteUserService siteUserService) {
        this.siteUserService = siteUserService;
    }

    @GetMapping(value = "/api/users/{userId}", produces = "application/json")
    public ResponseEntity<Object> getSiteUserInfo(@PathVariable Long userId) {
        try {
            SiteUser siteUser = siteUserService.getArbitraryUser(userId);
            return new ResponseEntity<>(siteUser, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage()+"\"", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/api/users/self", produces = "application/json")
    public ResponseEntity<Object> getSelfInfo() {
        try {
            SiteUser siteUser = siteUserService.getCurrentlyLoggedInSiteUser();

            return new ResponseEntity<>(siteUser, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage()+"\"", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/api/users/{userId}/accounts", produces = "application/json")
    public ResponseEntity<Object> getUsersAccounts(@PathVariable Long userId) {
        try {
            SiteUser siteUser = siteUserService.getArbitraryUser(userId);
            return new ResponseEntity<>(siteUser.getRegionalAccounts(), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/api/users/{userId}/teams", produces = "application/json")
    public ResponseEntity<Object> getUserTeams(@PathVariable Long userId) {
        try {
            SiteUser siteUser = siteUserService.getArbitraryUser(userId);
            return new ResponseEntity<>(siteUser.getUserTeams(), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error" + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/api/users/self/teams", produces = "application/json")
    public ResponseEntity<Object> getSelfTeams() {
        try {

            SiteUser siteUser = siteUserService.getCurrentlyLoggedInSiteUser();
            return new ResponseEntity<>(siteUser.getUserTeams(), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error" + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/api/users/changeName", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> changeUsername(@RequestBody Map<String, String> json) {
        try {
            if (json.get("newName").isBlank()) {
                return new ResponseEntity<>("\"No field can be left blank.\"", HttpStatus.BAD_REQUEST);
            }

            SiteUser siteUser = siteUserService.getCurrentlyLoggedInSiteUser();
            siteUserService.modifyNameAndSave(siteUser, json.get("newName"));

            return new ResponseEntity<>("\"Username succesfully changed.\"", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("\"Error, name already in use.\"", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/api/users/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> registerUser(@RequestBody SiteUserRegisterer registerUser) {

        try {
            if(siteUserService.registerUser(registerUser)){
                return new ResponseEntity<>("\"Successful registration.\"", HttpStatus.OK);
            }
            return new ResponseEntity<>("\"Error, username, password, or email cannot be empty.\"", HttpStatus.BAD_REQUEST);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("\"Error, email or username already in use.\"", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/api/users/password", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> changePassword(@RequestBody Map<String, String> json) {
        try {
            SiteUser siteUser = siteUserService.getCurrentlyLoggedInSiteUser();
            if (siteUserService.changePassword(siteUser, json.get("oldPassword"), json.get("newPassword")))
            {
                return new ResponseEntity<>("\"Successfully changed password.\"", HttpStatus.OK);
            }
            return new ResponseEntity<>("\"Error, mismatching passwords.\"", HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping(value = "/api/users/delete", produces = "application/json")
    public ResponseEntity<Object> deleteAccount(@RequestBody Map<String, String> json) {

        try {
            SiteUser siteUser = siteUserService.getCurrentlyLoggedInSiteUser();

            if (siteUserService.deleteAccount(siteUser, json.get("password"))){
                return new ResponseEntity<>("\"User deleted.\"", HttpStatus.OK);
            }
            return new ResponseEntity<>("\"Password is wrong.\"", HttpStatus.BAD_REQUEST);
        }catch(ResourceNotFoundException e){
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }
}
