package com.egyetem.szakdolgozat.ranking;

import com.egyetem.szakdolgozat.database.tournament.persistance.Tournament;
import com.egyetem.szakdolgozat.database.tournament.service.TournamentService;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeams;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;
import com.egyetem.szakdolgozat.notify.NotificationServiceImpl;
import com.egyetem.szakdolgozat.ranking.Service.RankingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RankingController {

    private final TournamentService tournamentService;
    private final RankingService rankingService;
    private final TaskExecutor executor;

    @Autowired
    public RankingController(TournamentService tournamentService, @Qualifier("taskExecutor") TaskExecutor taskExecutor,
                             RankingService rankingService) {
        this.tournamentService = tournamentService;
        this.executor = taskExecutor;
        this.rankingService = rankingService;
    }
    Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @PutMapping(value = "/api/update/rankings/{tournamentId}", produces = "application/json")
    public ResponseEntity<String> getRanks(@PathVariable Long tournamentId) {

        try {
            Tournament tournament = tournamentService.getById(tournamentId);

            if (tournament.getUpdating()) {
                return new ResponseEntity<>("\"Error, tournament is under update. Try later.\"", HttpStatus.CONFLICT);
            }
            //Lazy loading does not work from side threads so this is here to ensure all needed data is loaded.
            for (TournamentToTeams tournamentToTeams : tournament.getTeams()) {
                for (SiteUser user : tournamentToTeams.getTeam().getTeamMembers()) {
                    user.getRegionalAccountByRegion(tournament.getRegionId());
                }
            }

            logger.info("RANK UPDATE STARTING: " + System.currentTimeMillis());

            executor.execute(() -> rankingService.updateRank(tournament));


            return new ResponseEntity<>("\" Update request recieved. Processing has begun.\"", HttpStatus.ACCEPTED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("\"Error: " + e.getMessage() + "\"", HttpStatus.NOT_FOUND);
        }
    }
}
