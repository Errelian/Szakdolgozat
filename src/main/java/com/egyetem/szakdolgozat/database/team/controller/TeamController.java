package com.egyetem.szakdolgozat.database.team.controller;

import com.egyetem.szakdolgozat.database.team.persistance.Team;
import com.egyetem.szakdolgozat.database.team.persistance.TeamRepository;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TeamController {

    TeamRepository teamRepository;
    SiteUserRepository siteUserRepository;

    @Autowired
    public TeamController(TeamRepository teamRepository, SiteUserRepository siteUserRepository) {
        this.teamRepository = teamRepository;
        this.siteUserRepository = siteUserRepository;
    }

    @PostMapping(value = "/api/teams/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createNewTeam(@RequestBody Map<String, String> json) {

        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            SiteUser siteUser = siteUserRepository.findSiteUserByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

            if (!json.get("teamName").isBlank()) {
                Team team = new Team(json.get("teamName"), siteUser.getId());

                teamRepository.save(team);
                return new ResponseEntity<>("\"Successfully created new team.\"", HttpStatus.OK);
            }
            return new ResponseEntity<>("\"Error, team name cannot be blank.\"", HttpStatus.BAD_REQUEST);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("\"Name already in use.\"", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/api/teams/changeName", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> changeName(@RequestBody Map<String, String> json) {
        try {
            if (!(json.get("newName").isBlank())) {

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                SiteUser changer = siteUserRepository.findSiteUserByUsername(authentication.getName())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found."));

                Team team = teamRepository.findByTeamName(json.get("teamNameOld"))
                    .orElseThrow(() -> new ResourceNotFoundException("Team not found."));


                if (team.getCreatorId().equals(changer.getId())) {
                    team.setTeamName(json.get("newName"));
                    teamRepository.save(team);
                    return new ResponseEntity<>("\"Successfully changed team name.\"", HttpStatus.OK);
                }
                return new ResponseEntity<>("\"You are not the creator of that team\"", HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>("\"Error, the teamname or your id cannot be blank\"", HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/api/teams/delete/{teamId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> deleteTeam(@PathVariable Long teamId) {
        try {

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                SiteUser changer = siteUserRepository.findSiteUserByUsername(authentication.getName())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found."));

                Team team = teamRepository.findTeamById(teamId)
                    .orElseThrow(() -> new ResourceNotFoundException("Team not found."));


                if (team.getCreatorId().equals(changer.getId())) {
                    teamRepository.delete(team);
                    return new ResponseEntity<>("\"Successfully deleted team.\"", HttpStatus.OK);
                }
                return new ResponseEntity<>("\"You are not the creator of that team\"", HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/api/teams/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> addUser(@RequestBody Map<String, String> json) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            SiteUser user = siteUserRepository.findSiteUserByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found.")); //TODO
            Team team = teamRepository.findTeamById(Long.parseLong(json.get("teamId")))
                .orElseThrow(() -> new ResourceNotFoundException("Team not found."));

            team.getTeamMembers().add(user);

            teamRepository.save(team);
            return new ResponseEntity<>("\"Successfully added user.\"", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/api/teams/remove", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> removeUser(@RequestBody Map<String, String> json) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


            Team team = teamRepository.findTeamById(Long.parseLong(json.get("teamId")))
                .orElseThrow(() -> new ResourceNotFoundException("Team not found."));
            SiteUser user = siteUserRepository.findSiteUserByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));


            if (team.getTeamMembers().contains(user)) {
                team.getTeamMembers().remove(user);
            } else {
                return new ResponseEntity<>("\"You are not in this team\"", HttpStatus.FORBIDDEN);
            }
            teamRepository.save(team);
            return new ResponseEntity<>("\"Successfully removed user.\"", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/api/teams/all", produces = "application/json")
    public ResponseEntity<Object> getAllTeams() {
        try {
            List<Team> teams = teamRepository.findAll();

            return new ResponseEntity<>(teams, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/api/teams/{teamId}", produces = "application/json")
    public ResponseEntity<Object> getTeam(@PathVariable Long teamId) {
        try {
            Team team =
                teamRepository.findTeamById(teamId).orElseThrow(() -> new ResourceNotFoundException("Team not found."));

            return new ResponseEntity<>(team, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/api/teams/byname/{teamName}", produces = "application/json")
    public ResponseEntity<Object> getTeamByName(@PathVariable String teamName) {
        try {
            Team team =
                teamRepository.findByTeamName(teamName)
                    .orElseThrow(() -> new ResourceNotFoundException("Team not found."));

            return new ResponseEntity<>(team, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

}
