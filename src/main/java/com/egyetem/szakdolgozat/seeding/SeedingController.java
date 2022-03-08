package com.egyetem.szakdolgozat.seeding;

import com.egyetem.szakdolgozat.database.tournament.persistance.Tournament;
import com.egyetem.szakdolgozat.database.tournament.persistance.TournamentRepository;
import com.egyetem.szakdolgozat.database.tournament.service.TournamentService;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeams;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeamsCKey;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeamsRepository;
import com.egyetem.szakdolgozat.database.tournamentToTeams.service.TournamentToTeamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class SeedingController {

    TournamentService tournamentService;
    TournamentToTeamsService tournamentToTeamsService;

    @Autowired
    public SeedingController(TournamentService tournamentService,
                             TournamentToTeamsService tournamentToTeamsService) {
        this.tournamentService = tournamentService;
        this.tournamentToTeamsService = tournamentToTeamsService;
    }


    @PutMapping(value = "/api/seeding/{tournamentId}", produces = "application/json")
    public ResponseEntity<Object> seedTournament(@PathVariable Long tournamentId) {
        try {
            Tournament tournament = tournamentService.getById(tournamentId);

            ArrayList<ArrayList<TeamSkillDto>> matchups = Seeder.seedTournament(tournament);

            int i = 1;
            for (ArrayList<TeamSkillDto> teamSkillDtos : matchups) {
                for (TeamSkillDto team : teamSkillDtos) {
                    if(team.getId() != -1L) {
                        TournamentToTeams teamTable = tournamentToTeamsService.getById(team.getId(), tournamentId);
                        teamTable.setPosition(i);
                        tournamentToTeamsService.save(teamTable);
                    }
                    i++;
                }
            }

            return new ResponseEntity<>("\"Tournament Seeded.\"", HttpStatus.OK);
        }catch(ResourceNotFoundException e){
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

}
