package com.egyetem.szakdolgozat.user.controller;

import com.egyetem.szakdolgozat.region.persistance.RegionRepository;
import com.egyetem.szakdolgozat.regionalAccount.persistance.RegionalAccount;
import com.egyetem.szakdolgozat.team.persistance.Team;
import com.egyetem.szakdolgozat.team.persistance.TeamRepository;
import com.egyetem.szakdolgozat.tournament.persistance.TournamentRepository;
import com.egyetem.szakdolgozat.tournamentToTeams.persistance.TournamentToTeamsRepository;
import com.egyetem.szakdolgozat.user.persistance.SiteUser;
import com.egyetem.szakdolgozat.user.persistance.SiteUserPasswordChangerPojo;
import com.egyetem.szakdolgozat.user.persistance.SiteUserRegisterer;
import com.egyetem.szakdolgozat.user.persistance.SiteUserRepository;
import com.egyetem.szakdolgozat.user.persistance.SiteUserUsernameChanger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
public class SiteUserController {

    SiteUserRepository siteUserRepository;
    TeamRepository teamRepository;

    @Autowired
    public SiteUserController(SiteUserRepository siteUserRepository, TeamRepository teamRepository) {
        this.siteUserRepository = siteUserRepository;
        this.teamRepository = teamRepository;
    }

    @GetMapping(value = "/users/{userId}") //TODO REMOVE PASSWORD FROM THIS IN A PRETTY WAY
    public SiteUser getSiteUserInfo(@PathVariable Long userId) {
        return siteUserRepository.findUserById(userId);
    }

    @GetMapping(value = "/users/{userId}/accounts")
    public Set<RegionalAccount> getUsersAccounts(@PathVariable Long userId) {
        return siteUserRepository.findUserById(userId).getRegionalAccounts();
    }

    @GetMapping(value = "/users/{userId}/teams")
    public Set<Team> getUserTeams(@PathVariable Long userId) {
        return teamRepository.findByTeamMembers_Id(userId);
    }

    @PutMapping(value = "/users/changeName", consumes = "application/json")
    public void changeUsername(@RequestBody SiteUserUsernameChanger siteUserUsernameChanger) {
        SiteUser tempUser = siteUserRepository.findUserById(siteUserUsernameChanger.getUserId());

        tempUser.setUsername(siteUserUsernameChanger.getNewName());

        siteUserRepository.save(tempUser);
    }

    @PostMapping(value = "/users/register", consumes = "application/json")
    public String registerUser(@RequestBody SiteUserRegisterer registerUser) {

        if (!(registerUser.getUsername().isBlank() || registerUser.getPassword().isBlank() ||
            registerUser.geteMail().isBlank())) {

            SiteUser user = new SiteUser(registerUser.getUsername(), registerUser.getPassword(),
                registerUser.geteMail()); //TODO add pw hashing to this

            siteUserRepository.save(user);
            return "Sucessfull registration.";
        }
        return "Error, username, password, or email cannot be empty.";
    }

    @PutMapping(value = "/users/{userId}/password", consumes = "application/json")
    public String changePassword(@PathVariable Long userId, @RequestBody SiteUserPasswordChangerPojo passwords) {
        SiteUser currentUser = siteUserRepository.findUserById(userId);

        System.out.println(currentUser.getPassword());
        String out;
        if (currentUser.getPassword().equals(passwords.getOldPassword())) //TODO add a hashing to this
        {
            currentUser.setPassword(passwords.getNewPassword());
            siteUserRepository.save(currentUser);
            out = "Successfully changed password.";
        } else {
            out = "Error, mismatching passwords.";
        }
        System.out.println(currentUser.getPassword());

        return out;
    }

    @DeleteMapping(value = "/users/{userId}/delete")
    public String deleteAccount(@PathVariable Long userId, @RequestBody Map<String, String> json) {

        SiteUser currentUser = siteUserRepository.findUserById(userId);

        if (currentUser.getPassword().equals(json.get("password"))) { //TODO ADD HASHING TO THIS
            siteUserRepository.delete(currentUser);
            return "User deleted.";
        }
        return "Mismatching passwords.";
    }
}
