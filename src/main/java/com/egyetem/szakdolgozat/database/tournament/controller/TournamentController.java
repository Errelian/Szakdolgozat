package com.egyetem.szakdolgozat.database.tournament.controller;

import com.egyetem.szakdolgozat.database.team.persistance.Team;
import com.egyetem.szakdolgozat.database.team.persistance.TeamRepository;
import com.egyetem.szakdolgozat.database.tournament.persistance.Tournament;
import com.egyetem.szakdolgozat.database.tournament.persistance.TournamentRepository;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeams;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeamsCKey;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeamsRepository;
import com.egyetem.szakdolgozat.notify.Notifier;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class TournamentController {

    private TournamentRepository tournamentRepository;
    private TournamentToTeamsRepository tournamentToTeamsRepository;
    private TeamRepository teamRepository;
    private TaskExecutor executor;

    @Autowired
    public TournamentController(TournamentRepository tournamentRepository,
                                TournamentToTeamsRepository tournamentToTeamsRepository,
                                TeamRepository teamRepository,
                                @Qualifier("taskExecutor") TaskExecutor taskExecutor) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentToTeamsRepository = tournamentToTeamsRepository;
        this.teamRepository = teamRepository;
        this.executor = taskExecutor;
    }

    @PostMapping(value = "/api/tournament/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createNewTournament(
        @RequestBody Tournament tournament) {
        try {
            if (tournament.getTournamentName().isBlank() || tournament.getCreatorId().toString().isBlank() ||
                tournament.getRegionId().isBlank() || tournament.getStartTime().toString().isBlank()) {
                return new ResponseEntity<>(
                    "\"Error, no field can be empty, null, or compromised of only whitespaces.\"",
                    HttpStatus.BAD_REQUEST);
            }

            tournamentRepository.save(tournament);
            return new ResponseEntity<>("\"Saved tournament.\"", HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("\"Tournament name already in use.\"", HttpStatus.BAD_REQUEST);
        }
    }


    //TODO spring secu/oauth2: check if logged in id == creatorId
    @DeleteMapping(value = "/api/tournament/delete", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> deleteTournament(@RequestBody Map<String, Long> json) {
        tournamentRepository.deleteById(json.get("tournamentId"));
        return new ResponseEntity<>("\"Deleted tournament.\"", HttpStatus.OK);
    }

    //TODO spring secu/oauth2: check if logged in id == creatorID
    @PutMapping(value = "/api/tournament/change/name", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> changeTournamentName(@RequestBody Map<String, String> json) {

        try {
            if (json.get("newName").isBlank() || json.get("oldName").isBlank()) {
                return new ResponseEntity<>("\"Names cannot be blank\"", HttpStatus.BAD_REQUEST);
            }
            Tournament tournament = tournamentRepository.findByTournamentName(json.get("oldName"))
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found."));


            tournament.setTournamentName(json.get("newName"));
            tournamentRepository.save(tournament);

            return new ResponseEntity<>("\"Saved tournament name change.\"", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("\"Error, name already in use.\"", HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping(value = "/api/tournament/change/victor", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> changeVictor(@RequestBody Map<String, String> json) {

        try {
            if (json.get("victorId").isBlank() || json.get("tournamentName").isBlank()) {
                return new ResponseEntity<>("\"VictorID or tournamentName cannot be blank\"", HttpStatus.BAD_REQUEST);
            }
            Tournament tournament = tournamentRepository.findByTournamentName(json.get("tournamentName"))
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found."));


            tournament.setVictorId(Long.parseLong(json.get("victorId")));
            tournamentRepository.save(tournament);


            List<Team> teams = new ArrayList<>();
            for (TournamentToTeams tournamentToTeams : tournament.getTeams()){
                teams.add(tournamentToTeams.getTeam());
            }
            for (Team team : teams){
                team.getTeamMembers();
            }

            executor.execute(()-> {
                try {
                    Notifier.notifyUsers(tournament);
                } catch (UnirestException e) {
                    e.printStackTrace();
                }
            });

            return new ResponseEntity<>("\"Saved victor change.\"", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/api/tournament/change/region", consumes = "application/json", produces = "application/json") //TODO EXCEPTION HANDLING
    public ResponseEntity<String> changeRegion(@RequestBody Map<String, String> json) {

        try {
            if (json.get("regionId").isBlank() || json.get("tournamentName").isBlank()) {
                return new ResponseEntity<>("\"regionId or tournamentId cannot be blank\"", HttpStatus.BAD_REQUEST);
            }
            Tournament tournament = tournamentRepository.findByTournamentName(json.get("tournamentName"))
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found."));

            if (tournament.getUpdating()){
                return new ResponseEntity<>("\"Error, tournament is under update. Try later.\"", HttpStatus.CONFLICT);
            }

            tournament.setRegionId(json.get("regionId"));
            tournamentRepository.save(tournament);

            return new ResponseEntity<>("\"Saved regionID change.\"", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/api/tournament/get/all", produces = "application/json")
    public ResponseEntity<Object> getAll() {
        return new ResponseEntity<>(tournamentRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/api/tournament/get/teams/{tournamentId}", produces = "application/json")
    public ResponseEntity<Object> getTeams(@PathVariable Long tournamentId) {
        try {
            Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found."));

            return new ResponseEntity<>(tournament.getTeams(), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/api/tournament/get/{tournamentId}", produces = "application/json")
    public ResponseEntity<Object> getTournament(@PathVariable Long tournamentId) {
        try {
            Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found."));

            return new ResponseEntity<>(tournament, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error:" + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/api/tournament/add/team", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> addTeam(
        @RequestBody Map<String, String> json) { //TODO ADD EXCEPTION HANDLING TO IT, CONSTRAINT MOST LIKELY

        try {
            if (json.get("teamId").isBlank() || json.get("tournamentId").isBlank()) {
                return ResponseEntity.badRequest().body("No field can be left blank.");
            }
            TournamentToTeams tournamentToTeams = new TournamentToTeams(
                new TournamentToTeamsCKey(Long.parseLong(json.get("teamId")), Long.parseLong(json.get("tournamentId")))
            );

            Tournament tournament = tournamentRepository.findById(Long.parseLong(json.get("tournamentId"))).orElseThrow(
                () -> new ResourceNotFoundException("Tournament not found."));

            if (tournament.getUpdating()){
                return new ResponseEntity<>("\"Error, tournament is under update. Try later.\"", HttpStatus.CONFLICT);
            }

            Team team = teamRepository.findTeamById(Long.parseLong(json.get("teamId"))).orElseThrow(
                () -> new ResourceNotFoundException("Team not found."));

            tournamentToTeams.setTeam(team);
            tournamentToTeams.setTournament(tournament);

            tournamentToTeamsRepository.save(tournamentToTeams);
            return ResponseEntity.ok("\"Successfully saved.\"");
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("Resource not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Bad input: ", HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    @DeleteMapping(value = "/api/tournament/remove/team", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> removeTeam(@RequestBody Map<String, String> json) {

        try {
            if (json.get("teamId").isBlank() || json.get("tournamentId").isBlank()) {
                return new ResponseEntity<>("\"No field can be left blank.\"", HttpStatus.BAD_REQUEST);
            }
            TournamentToTeams tournamentToTeams = new TournamentToTeams(
                new TournamentToTeamsCKey(Long.parseLong(json.get("teamId")),
                    Long.parseLong(json.get("tournamentId"))));


            Tournament tournament = tournamentRepository.findById(Long.parseLong(json.get("tournamentId"))).orElseThrow(
                () -> new ResourceNotFoundException("Tournament not found."));

            if (tournament.getUpdating()){
                return new ResponseEntity<>("\"Error, tournament is under update. Try later.\"", HttpStatus.CONFLICT);
            }

            Team team = teamRepository.findTeamById(Long.parseLong(json.get("teamId"))).orElseThrow(
                () -> new ResourceNotFoundException("Team not found."));


            tournamentToTeams.setTeam(team);
            tournamentToTeams.setTournament(tournament);

            tournamentToTeamsRepository.delete(tournamentToTeams);
            return new ResponseEntity<>("\"Successfully deleted.\"", HttpStatus.OK);
        }catch(ResourceNotFoundException e){
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

    //Position can only be modified by the backend, so this is only for elimination
    @PutMapping(value = "api/tournament/modify/team", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> eliminateTeam(@RequestBody Map<String, Long> json) {

        try {
            if (json.get("eliminationRound") > 0L || json.get("tournamentId") > 0L || json.get("teamId") > 0L) {
                TournamentToTeams tournamentToTeams = tournamentToTeamsRepository
                    .findTournamentToTeamsById(new TournamentToTeamsCKey(json.get("teamId"), json.get("tournamentId")))
                    .orElseThrow(() -> new ResourceNotFoundException("Tournament not found."));

                tournamentToTeams.setEliminationRound(json.get("eliminationRound").intValue());

                tournamentToTeamsRepository.save(tournamentToTeams);

                return new ResponseEntity<>("\"Successfully modifed.\"", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("\"Error, no field can be smaller than 0 or left blank.\"",
                    HttpStatus.BAD_REQUEST);
            }
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

}
