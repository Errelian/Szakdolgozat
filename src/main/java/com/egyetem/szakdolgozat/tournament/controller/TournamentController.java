package com.egyetem.szakdolgozat.tournament.controller;

import com.egyetem.szakdolgozat.team.persistance.Team;
import com.egyetem.szakdolgozat.team.persistance.TeamRepository;
import com.egyetem.szakdolgozat.tournament.persistance.Tournament;
import com.egyetem.szakdolgozat.tournament.persistance.TournamentRepository;
import com.egyetem.szakdolgozat.tournamentToTeams.persistance.TournamentToTeams;
import com.egyetem.szakdolgozat.tournamentToTeams.persistance.TournamentToTeamsCKey;
import com.egyetem.szakdolgozat.tournamentToTeams.persistance.TournamentToTeamsRepository;
import org.hibernate.exception.ConstraintViolationException;
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
import java.util.Optional;
import java.util.Set;

@RestController
public class TournamentController {

    TournamentRepository tournamentRepository;
    TournamentToTeamsRepository tournamentToTeamsRepository;
    TeamRepository teamRepository;

    @Autowired
    public TournamentController(TournamentRepository tournamentRepository,
                                TournamentToTeamsRepository tournamentToTeamsRepository,
                                TeamRepository teamRepository) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentToTeamsRepository = tournamentToTeamsRepository;
        this.teamRepository = teamRepository;
    }

    @PostMapping(value = "/api/tournament/create", consumes = "application/json")
    public String createNewTournament(
        @RequestBody Tournament tournament) { //TODO EXCEPTION HANDLING THIS APPLIES TO THE ENTIRE FILE

        if (tournament.getTournamentName().isBlank() || tournament.getCreatorId().toString().isBlank() ||
            tournament.getRegionId().isBlank() || tournament.getStartTime().toString().isBlank()) {
            return "Error, no field can be empty, null, or compromised of only whitespaces.";
        }

        tournamentRepository.save(tournament);
        return "Saved tournament.";
    }


    //spring secu: check if logged in id == creatorId TODO ADD EXCEPTION HANDLING
    @DeleteMapping(value = "/api/tournament/delete", consumes = "application/json")
    public String deleteTournament(@RequestBody Map<String, Long> json) {
        tournamentRepository.deleteById(json.get("tournamentId"));
        return "Deleted tournament.";
    }

    @PutMapping(value = "/api/tournament/change/name", consumes = "application/json") //TODO EXCEPTION HANDLING
    public String changeTournamentName(@RequestBody Map<String, String> json) {

        if (json.get("newName").isBlank() || json.get("oldName").isBlank()) {
            return "Names cannot be blank";
        }
        Optional<Tournament> tournament = tournamentRepository.findByTournamentName(json.get("oldName"));


        if (tournament.isPresent()) {
            tournament.get().setTournamentName(json.get("newName"));
            tournamentRepository.save(tournament.get());

            return "Saved tournament name change.";
        }

        return "Error, tournament no tournament called, " + json.get("oldName") + " found.";
    }


    @PutMapping(value = "/api/tournament/change/victor", consumes = "application/json") //TODO EXCEPTION HANDLING
    public String changeVictor(@RequestBody Map<String, String> json) {

        if (json.get("victorId").isBlank() || json.get("tournamentName").isBlank()) {
            return "victorID or tournamentName cannot be blank";
        }
        Optional<Tournament> tournament = tournamentRepository.findByTournamentName(json.get("tournamentName"));


        if (tournament.isPresent()) {
            tournament.get().setVictorId(Long.parseLong(json.get("victorId")));
            tournamentRepository.save(tournament.get());

            return "Saved victor change.";
        }

        return "Error, tournament no tournament called, " + json.get("tournamentName") + " found.";
    }

    @PutMapping(value = "/api/tournament/change/region", consumes = "application/json") //TODO EXCEPTION HANDLING
    public String changeRegion(@RequestBody Map<String, String> json) {

        if (json.get("regionId").isBlank() || json.get("tournamentName").isBlank()) {
            return "regionId or tournamentId cannot be blank";
        }
        Optional<Tournament> tournament = tournamentRepository.findByTournamentName(json.get("tournamentName"));


        if (tournament.isPresent()) {
            tournament.get().setRegionId(json.get("regionId"));
            tournamentRepository.save(tournament.get());

            return "Saved regionID change.";
        }

        return "Error, tournament no tournament called, " + json.get("tournamentName") + " found.";
    }

    @GetMapping(value = "/api/tournament/get/all")
    public List<Tournament> getAll() {
        return tournamentRepository.findAll();
    }

    @GetMapping(value = "/api/tournament/get/teams/{tournamentId}")
    public Set<TournamentToTeams> getTeams(@PathVariable Long tournamentId) {
        return tournamentRepository.findById(tournamentId).get().getTeams();
    }

    @GetMapping(value = "/api/tournament/get/{tournamentId}")
    public Optional<Tournament> getTournament(@PathVariable Long tournamentId) {
        return tournamentRepository.findById(tournamentId);
    }

    @PostMapping(value = "/api/tournament/add/team", consumes = "application/json")
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

            Team team = teamRepository.findTeamById(Long.parseLong(json.get("teamId"))).orElseThrow(
                () -> new ResourceNotFoundException("Team not found."));

            tournamentToTeams.setTeam(team);
            tournamentToTeams.setTournament(tournament);

            tournamentToTeamsRepository.save(tournamentToTeams);
            return ResponseEntity.ok("Successfully saved.");
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("Resource not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e){
            return new ResponseEntity<>("Bad input: ", HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    @DeleteMapping(value = "/api/tournament/remove/team", consumes = "application/json")
    public String removeTeam(
        @RequestBody Map<String, String> json) { //TODO ADD EXCEPTION HANDLING TO IT, CONSTRAINT MOST LIKELY

        if (json.get("teamId").isBlank() || json.get("tournamentId").isBlank()) {
            return "No field can be left blank.";
        }
        TournamentToTeams tournamentToTeams = new TournamentToTeams(
            new TournamentToTeamsCKey(Long.parseLong(json.get("teamId")), Long.parseLong(json.get("tournamentId"))));

        Optional<Tournament> tournament = tournamentRepository.findById(Long.parseLong(json.get("tournamentId")));

        Optional<Team> team = teamRepository.findTeamById(Long.parseLong(json.get("teamId")));

        if (tournament.isPresent()) {
            if (team.isPresent()) {

                tournamentToTeams.setTeam(team.get());
                tournamentToTeams.setTournament(tournament.get());

                tournamentToTeamsRepository.delete(tournamentToTeams);
                return "Successfully deleted.";
            } else {
                return "No such team found";
            }
        } else {
            return "No such tournament found.";
        }
    }

}
