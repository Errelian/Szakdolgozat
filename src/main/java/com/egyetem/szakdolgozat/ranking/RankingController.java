package com.egyetem.szakdolgozat.ranking;

import com.egyetem.szakdolgozat.database.regionalAccount.persistance.RegionalAccountRepository;
import com.egyetem.szakdolgozat.database.tournament.persistance.Tournament;
import com.egyetem.szakdolgozat.database.tournament.persistance.TournamentRepository;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeams;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RankingController {

    private final TournamentRepository tournamentRepository;
    private final RegionalAccountRepository regionalAccountRepository;
    private TaskExecutor executor;

    @Autowired
    public RankingController(TournamentRepository tournamentRepository, RegionalAccountRepository regionalAccountRepository, TaskExecutor taskExecutor){
        this.tournamentRepository = tournamentRepository;
        this.regionalAccountRepository = regionalAccountRepository;
        this.executor = taskExecutor;
    }

    @PutMapping(value = "/api/update/rankings/{tournamentId}")
    public ResponseEntity<String> testRanker(@PathVariable Long tournamentId) {

        try {
            Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found"));

            for (TournamentToTeams tournamentToTeams : tournament.getTeams()) {
                for (SiteUser user : tournamentToTeams.getTeam().getTeamMembers()) {
                    user.getRegionalAccountByRegion(tournament.getRegionId());
                }
            }

            RankingService rankingService = new RankingService(tournamentRepository, regionalAccountRepository);

            System.out.println("UPDATE STARTING");

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    rankingService.updateRank(tournament);
                }
            });


            System.out.println("UPDATE OVER");
            return new ResponseEntity<>("\" Update request recieved. Processing has begun.\"", HttpStatus.ACCEPTED);
        } catch(ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }
}
