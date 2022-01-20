package com.egyetem.szakdolgozat.user.controller;

import com.egyetem.szakdolgozat.user.persistance.SiteUser;
import com.egyetem.szakdolgozat.user.persistance.SiteUserRegisterer;
import com.egyetem.szakdolgozat.user.persistance.SiteUserRepository;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SiteUserController {

    SiteUserRepository siteUserRepository;

    @Autowired
    public SiteUserController(SiteUserRepository siteUserRepository) {
        this.siteUserRepository = siteUserRepository;
    }

    @GetMapping(value = "/api/users/{userId}", produces = "application/json")
    public ResponseEntity<Object> getSiteUserInfo(@PathVariable Long userId) {
        try {
            SiteUser siteUser = siteUserRepository.findUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            return new ResponseEntity<>(siteUser, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage()+"\"", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/api/users/{userId}/accounts", produces = "application/json")
    public ResponseEntity<Object> getUsersAccounts(@PathVariable Long userId) {
        try {
            SiteUser siteUser = siteUserRepository.findUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            return new ResponseEntity<>(siteUser.getRegionalAccounts(), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/api/users/{userId}/teams", produces = "application/json")
    public ResponseEntity<Object> getUserTeams(@PathVariable Long userId) {
        try {
            SiteUser siteUser = siteUserRepository.findUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            return new ResponseEntity<>(siteUser.getUserTeams(), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error" + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/api/users/changeName", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> changeUsername(@RequestBody Map<String, String> json) {
        try {
            if (json.get("newName").isBlank() || json.get("userId").isBlank()) {
                return new ResponseEntity<>("\"No field can be left blank.\"", HttpStatus.BAD_REQUEST);
            }
            SiteUser tempUser = siteUserRepository.findUserById(Long.parseLong(json.get("userId")))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            tempUser.setUsername(json.get("newName"));

            siteUserRepository.save(tempUser);

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
            if (!(registerUser.getUsername().isBlank() || registerUser.getPassword().isBlank() ||
                registerUser.geteMail().isBlank())) {

                SiteUser user = new SiteUser(registerUser.getUsername(), registerUser.getPassword(),
                    registerUser.geteMail()); //TODO add pw hashing to this

                siteUserRepository.save(user);
                return new ResponseEntity<>("\"Sucessfull registration.\"", HttpStatus.OK);
            }
            return new ResponseEntity<>("\"Error, username, password, or email cannot be empty.\"", HttpStatus.BAD_REQUEST);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("\"Error, email or username already in use\"", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/api/users/{userId}/password", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> changePassword(@PathVariable Long userId, @RequestBody Map<String, String> json) {

        try {
            SiteUser currentUser = siteUserRepository.findUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")); //TODO

            if (currentUser.getPassword().equals(json.get("oldPassword"))) //TODO add a hashing to this
            {
                currentUser.setPassword(json.get("newPassword"));
                siteUserRepository.save(currentUser);
                return new ResponseEntity<>("\"Successfully changed password.\"", HttpStatus.OK);
            }
            return new ResponseEntity<>("\"Error, mismatching passwords.\"", HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping(value = "/api/users/{userId}/delete", produces = "application/json")
    public ResponseEntity<Object> deleteAccount(@PathVariable Long userId, @RequestBody Map<String, String> json) {

        try {
            SiteUser currentUser =
                siteUserRepository.findUserById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            if (currentUser.getPassword().equals(json.get("password"))) { //TODO ADD HASHING TO THIS
                siteUserRepository.delete(currentUser);
                return new ResponseEntity<>("\"User deleted.\"", HttpStatus.OK);
            }
            return new ResponseEntity<>("\"Password is wrong.\"", HttpStatus.BAD_REQUEST);
        }catch(ResourceNotFoundException e){
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }
}
