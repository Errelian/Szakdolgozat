package com.egyetem.szakdolgozat.seeding;

import com.egyetem.szakdolgozat.tournament.persistance.Tournament;
import com.egyetem.szakdolgozat.tournament.persistance.TournamentRepository;
import com.egyetem.szakdolgozat.tournamentToTeams.persistance.TournamentToTeams;
import com.egyetem.szakdolgozat.tournamentToTeams.persistance.TournamentToTeamsCKey;
import com.egyetem.szakdolgozat.tournamentToTeams.persistance.TournamentToTeamsRepository;
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

    TournamentRepository tournamentRepository;
    TournamentToTeamsRepository tournamentToTeamsRepository;

    @Autowired
    public SeedingController(TournamentRepository tournamentRepository,
                             TournamentToTeamsRepository tournamentToTeamsRepository) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentToTeamsRepository = tournamentToTeamsRepository;
    }


    @PutMapping(value = "/api/seeding/{tournamentId}", produces = "application/json")
    public ResponseEntity<Object> seedTournament(@PathVariable Long tournamentId) {
        try {
            Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found."));

            ArrayList<ArrayList<TeamSkillDto>> matchups = Seeder.seedTournament(tournament);

            int i = 0;
            for (ArrayList<TeamSkillDto> teamSkillDtos : matchups) {
                for (TeamSkillDto team : teamSkillDtos) {
                    TournamentToTeams teamTable =
                        tournamentToTeamsRepository.findTournamentToTeamsById(new TournamentToTeamsCKey(team.getId(), tournamentId)).get();

                    teamTable.setPosition(i);
                    tournamentToTeamsRepository.save(teamTable);
                    i++;
                }
            }

            return new ResponseEntity<>("\"Tournament Seeded.\"", HttpStatus.OK);
        }catch(ResourceNotFoundException e){
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }

}
