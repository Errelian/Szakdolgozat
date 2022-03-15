package com.egyetem.szakdolgozat.database.tournament.controller;

import com.egyetem.szakdolgozat.database.team.persistance.Team;
import com.egyetem.szakdolgozat.database.team.service.TeamService;
import com.egyetem.szakdolgozat.database.tournament.persistance.Tournament;
import com.egyetem.szakdolgozat.database.tournament.service.TournamentService;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeams;
import com.egyetem.szakdolgozat.database.tournamentToTeams.service.TournamentToTeamsService;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;
import com.egyetem.szakdolgozat.database.user.service.SiteUserService;
import com.egyetem.szakdolgozat.util.UnauthorizedException;
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
public class TournamentController {

    private final TournamentToTeamsService tournamentToTeamsService;
    private final TeamService teamService;
    private final SiteUserService siteUserService;
    private final TournamentService tournamentService;

    @Autowired
    public TournamentController(TournamentToTeamsService tournamentToTeamsService,
                                TeamService teamService,
                                SiteUserService siteUserService,
                                TournamentService tournamentService) {
        this.tournamentToTeamsService = tournamentToTeamsService;
        this.teamService = teamService;
        this.siteUserService = siteUserService;
        this.tournamentService = tournamentService;
    }

    @PostMapping(value = "/api/tournament/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createNewTournament(@RequestBody Tournament tournament) {
        try {
            if (!tournamentService.validate(tournament)) {
                return new ResponseEntity<>(
                    "\"Error, no field can be empty, null, or compromised of only whitespaces.\"",
                    HttpStatus.BAD_REQUEST);
            }

            SiteUser siteUser = siteUserService.getCurrentlyLoggedInSiteUser();
            tournamentService.setCreatorAndSave(tournament, siteUser);

            return new ResponseEntity<>("\"Saved tournament.\"", HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("\"Tournament name already in use.\"", HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping(value = "/api/tournament/delete", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> deleteTournament(@RequestBody Map<String, Long> json) {
        try {
            SiteUser siteUser = siteUserService.getCurrentlyLoggedInSiteUser();
            Tournament tournament = tournamentService.getById(json.get("tournamentId"));

            tournamentService.validateAndDelete(tournament, siteUser);
            return new ResponseEntity<>("\"Deleted tournament.\"", HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        } catch (IllegalAccessException e){
            return new ResponseEntity<>("\"Forbidden: You are not the creator of the tournament.\"", HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping(value = "/api/tournament/change/name", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> changeTournamentName(@RequestBody Map<String, String> json) {

        try {
            if (json.get("newName").isBlank() || json.get("oldName").isBlank()) {
                return new ResponseEntity<>("\"Names cannot be blank\"", HttpStatus.BAD_REQUEST);
            }

            SiteUser siteUser = siteUserService.getCurrentlyLoggedInSiteUser();
            Tournament tournament = tournamentService.getByName(json.get("oldName"));

            tournamentService.validateAndSaveNewName(tournament, siteUser, json.get("newName"));
            return new ResponseEntity<>("\"Saved tournament name change.\"", HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("\"Error, name already in use.\"", HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException e){
            return new ResponseEntity<>("\"Forbidden: You are not the creator of the tournament.\"", HttpStatus.FORBIDDEN);
        }
    }


    @PutMapping(value = "/api/tournament/change/victor", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> changeVictor(@RequestBody Map<String, String> json) {

        try {
            if (json.get("victorId").isBlank() || json.get("tournamentName").isBlank()) {
                return new ResponseEntity<>("\"VictorID or tournamentName cannot be blank\"", HttpStatus.BAD_REQUEST);
            }

            SiteUser siteUser = siteUserService.getCurrentlyLoggedInSiteUser();
            Tournament tournament = tournamentService.getById(Long.parseLong(json.get("tournamentId")));


            tournamentService.validateAndSaveVictor(tournament, siteUser, Long.parseLong(json.get("victorId")));
            tournamentService.emailSenderService(tournament);
            return new ResponseEntity<>("\"Saved victor change.\"", HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        } catch (UnauthorizedException e){
            return new ResponseEntity<>("\"Forbidden: You are not the creator of the tournament.\"", HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping(value = "/api/tournament/change/region", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> changeRegion(@RequestBody Map<String, String> json) {

        try {
            if (json.get("regionId").isBlank() || json.get("tournamentName").isBlank()) {
                return new ResponseEntity<>("\"regionId or tournamentId cannot be blank\"", HttpStatus.BAD_REQUEST);
            }

            Tournament tournament = tournamentService.getByName(json.get("tournamentName"));
            if (tournament.getUpdating()) {
                return new ResponseEntity<>("\"Error, tournament is under update. Try later.\"", HttpStatus.CONFLICT);
            }

            SiteUser siteUser = siteUserService.getCurrentlyLoggedInSiteUser();

            tournamentService.validateAndChangeRegion(tournament, siteUser, json.get("regionId"));
            return new ResponseEntity<>("\"Saved regionID change.\"", HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        } catch (UnauthorizedException e){
            return new ResponseEntity<>("\"Forbidden: You are not the creator of the tournament.\"", HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping(value = "/api/tournament/get/all", produces = "application/json")
    public ResponseEntity<Object> getAll() {
        return new ResponseEntity<>(tournamentService.getAll(), HttpStatus.OK)  ;
    }

    @GetMapping(value = "/api/tournament/get/teams/{tournamentId}", produces = "application/json")
    //TODO HANDLE TOURNAMENTS OF SIZE 1
    public ResponseEntity<Object> getTeams(@PathVariable Long tournamentId) {
        try {
            Tournament tournament = tournamentService.getById(tournamentId);

            if (tournament.getTeams().size() <= 1) {
                return new ResponseEntity<>(tournament.getTeams(), HttpStatus.OK);
            }

            return new ResponseEntity<>(tournamentService.currentStandingCalculatorService(tournament), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/api/tournament/get/{tournamentId}", produces = "application/json")
    public ResponseEntity<Object> getTournament(@PathVariable Long tournamentId) {
        try {
            Tournament tournament = tournamentService.getById(tournamentId);

            return new ResponseEntity<>(tournament, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error:" + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/api/tournament/add/team", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> addTeam(
        @RequestBody Map<String, String> json) {

        try {
            if (json.get("teamId").isBlank() || json.get("tournamentId").isBlank()) {
                return ResponseEntity.badRequest().body("No field can be left blank.");
            }
            TournamentToTeams tournamentToTeams = tournamentToTeamsService
                .constructNewTournamentToTeams(Long.parseLong(json.get("teamId")), Long.parseLong(json.get("tournamentId")));

            Tournament tournament = tournamentService.getById(Long.parseLong(json.get("tournamentId")));

            if (tournament.getUpdating()) {
                return new ResponseEntity<>("\"Error, tournament is under update. Try later.\"", HttpStatus.CONFLICT);
            }

            SiteUser siteUser = siteUserService.getCurrentlyLoggedInSiteUser();
            Team team = teamService.getTeamById(Long.parseLong(json.get("teamId")));

            if (tournamentToTeamsService.validateAndSaveOrDelete(tournament, tournamentToTeams, team, siteUser, true)) {
                return ResponseEntity.ok("\"Successfully saved.\"");
            }
            return new ResponseEntity<>("\"Forbidden: You are not the creator of the team.\"", HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Resource not found: \"" + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("\"Bad input: \"", HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    @DeleteMapping(value = "/api/tournament/remove/team", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> removeTeam(@RequestBody Map<String, String> json) {

        try {
            if (json.get("teamId").isBlank() || json.get("tournamentId").isBlank()) {
                return new ResponseEntity<>("\"No field can be left blank.\"", HttpStatus.BAD_REQUEST);
            }
            TournamentToTeams tournamentToTeams = tournamentToTeamsService
                .constructNewTournamentToTeams(Long.parseLong(json.get("teamId")), Long.parseLong(json.get("tournamentId")));


            Tournament tournament = tournamentService.getById(Long.parseLong(json.get("tournamentId")));

            if (tournament.getUpdating()) {
                return new ResponseEntity<>("\"Error, tournament is under update. Try later.\"", HttpStatus.CONFLICT);
            }

            SiteUser siteUser = siteUserService.getCurrentlyLoggedInSiteUser();
            Team team = teamService.getTeamById(Long.parseLong(json.get("teamId")));

            if (tournamentToTeamsService.validateAndSaveOrDelete(tournament, tournamentToTeams, team, siteUser, false)) {
                return new ResponseEntity<>("\"Successfully deleted.\"", HttpStatus.OK);
            }
            return new ResponseEntity<>("\"Forbidden: You are not the creator of the team.\"", HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    //Position can only be modified by the backend, so this is only for elimination
    @PutMapping(value = "/api/tournament/modify/team", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> eliminateTeam(@RequestBody Map<String, Long> json) {

        try {
            if (json.get("eliminationRound") > 0L || json.get("tournamentId") > 0L || json.get("teamId") > 0L) {
                TournamentToTeams tournamentToTeams = tournamentToTeamsService
                    .getById(json.get("teamId"), json.get("tournamentId"));

                SiteUser siteUser = siteUserService.getCurrentlyLoggedInSiteUser();

                Tournament tournament = tournamentService.getById(json.get("tournamentId"));

                if (tournamentToTeamsService.validateAndEliminateTeam(tournament, tournamentToTeams,siteUser, json.get("eliminationRound"))) {
                    return new ResponseEntity<>("\"Successfully eliminated team.\"", HttpStatus.OK);
                }
                return new ResponseEntity<>("\"Forbidden: You are not the creator of the tournament.\"", HttpStatus.FORBIDDEN);

            } else {
                return new ResponseEntity<>("\"Error, no field can be smaller than 0 or left blank.\"",
                    HttpStatus.BAD_REQUEST);
            }
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

}