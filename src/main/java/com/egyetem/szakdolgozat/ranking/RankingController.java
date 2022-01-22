package com.egyetem.szakdolgozat.ranking;

import com.egyetem.szakdolgozat.database.regionalAccount.persistance.RegionalAccountRepository;
import com.egyetem.szakdolgozat.database.tournament.persistance.Tournament;
import com.egyetem.szakdolgozat.database.tournament.persistance.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RankingController {

    TournamentRepository tournamentRepository;
    RegionalAccountRepository regionalAccountRepository;

    @Autowired
    public RankingController(TournamentRepository tournamentRepository, RegionalAccountRepository regionalAccountRepository){
        this.tournamentRepository = tournamentRepository;
        this.regionalAccountRepository = regionalAccountRepository;
    }

    @PutMapping(value = "/api/update/rankings/{tournamentId}")
    public ResponseEntity<String> testRanker(@PathVariable Long tournamentId) {

        try {
            Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found"));

            RankingService rankingService = new RankingService(tournamentRepository, regionalAccountRepository);

            rankingService.updateRank(tournament);

            return new ResponseEntity<>("\" Update request recieved. Processing has begun.\"", HttpStatus.ACCEPTED);
        } catch(ResourceNotFoundException e){
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }

    }
}
