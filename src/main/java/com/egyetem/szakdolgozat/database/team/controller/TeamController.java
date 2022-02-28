package com.egyetem.szakdolgozat.database.team.controller;

import com.egyetem.szakdolgozat.database.team.persistance.Team;
import com.egyetem.szakdolgozat.database.team.service.TeamService;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeams;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeamsRepository;
import com.egyetem.szakdolgozat.database.tournamentToTeams.service.TournamentToTeamsService;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class TeamController {

    TeamService teamService;
    SiteUserService siteUserService;
    TournamentToTeamsService tournamentToTeamsService;

    @Autowired
    public TeamController(TeamService teamService, SiteUserService siteUserService,
                          TournamentToTeamsService tournamentToTeamsService) {
        this.teamService = teamService;
        this.siteUserService = siteUserService;
        this.tournamentToTeamsService = tournamentToTeamsService;
    }

    @PostMapping(value = "/api/teams/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createNewTeam(@RequestBody Map<String, String> json) {

        try {
            SiteUser siteUser = siteUserService.getCurrentlyLoggedInSiteUser();

            if (teamService.validateAndCreateNewTeam(json.get("teamName"), siteUser)) {
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

                SiteUser siteUser = siteUserService.getCurrentlyLoggedInSiteUser();
                Team team = teamService.getTeamByName(json.get("teamNameOld"));

                if (teamService.validateAndChangeName(team, siteUser, json.get("newName"))) {
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
            Team team = teamService.getTeamById(teamId);
            SiteUser siteUser = siteUserService.getCurrentlyLoggedInSiteUser();

            if (teamService.deleteTeam(team, siteUser)) {
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
            SiteUser siteUser = siteUserService.getCurrentlyLoggedInSiteUser();
            Team team = teamService.getTeamById(Long.parseLong(json.get("teamId")));

            teamService.addUserToTeam(team, siteUser);
            return new ResponseEntity<>("\"Successfully added user.\"", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/api/teams/remove", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> removeUser(@RequestBody Map<String, String> json) {
        try {

            Team team = teamService.getTeamById(Long.parseLong(json.get("teamId")));
            SiteUser siteUser = siteUserService.getCurrentlyLoggedInSiteUser();

            if (teamService.removeUserFromTeam(team, siteUser)) {
                return new ResponseEntity<>("\"Successfully removed user.\"", HttpStatus.OK);
            }
            return new ResponseEntity<>("\"You are not in this team\"", HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/api/teams/all", produces = "application/json")
    public ResponseEntity<Object> getAllTeams() {
        try {
            List<Team> teams = teamService.findAllTeams();

            return new ResponseEntity<>(teams, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/api/teams/{teamId}", produces = "application/json")
    public ResponseEntity<Object> getTeam(@PathVariable Long teamId) {
        try {
            Team team = teamService.getTeamById(teamId);

            return new ResponseEntity<>(team, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/api/teams/byname/{teamName}", produces = "application/json")
    public ResponseEntity<Object> getTeamByName(@PathVariable String teamName) {
        try {
            Team team = teamService.getTeamByName(teamName);

            return new ResponseEntity<>(team, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/api/teams/tournaments/{id}", produces = "application/json")
    public ResponseEntity<Object> getTeamTournaments(@PathVariable Long id) {
        try {
            Set<TournamentToTeams> tournaments = tournamentToTeamsService.getTournamentToTeamsById(id);
            return new ResponseEntity<>(tournaments, HttpStatus.OK);
        }catch(ResourceNotFoundException e){
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

}
