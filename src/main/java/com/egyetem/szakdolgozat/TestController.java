package com.egyetem.szakdolgozat;

import com.egyetem.szakdolgozat.seeding.Seeder;
import com.egyetem.szakdolgozat.seeding.TeamSkillDto;
import com.egyetem.szakdolgozat.team.persistance.Team;
import com.egyetem.szakdolgozat.team.persistance.TeamRepository;
import com.egyetem.szakdolgozat.tournament.persistance.TournamentRepository;
import com.egyetem.szakdolgozat.user.persistance.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    TeamRepository teamRepository;
    SiteUserRepository siteUserRepository;
    TournamentRepository tournamentRepository;

    @Autowired
    public TestController(TeamRepository teamRepository, SiteUserRepository siteUserRepository, TournamentRepository tournamentRepository) {
        this.teamRepository = teamRepository;
        this.siteUserRepository = siteUserRepository;
        this.tournamentRepository = tournamentRepository;
    }

    @GetMapping(value = "/api/test/seeder/teamskill")
    public void testTeamSkillSeeder(){
        Team team1 = teamRepository.findTeamById(15L).orElseThrow(() -> new ResourceNotFoundException("Team not found."));
        Team team2 = teamRepository.findTeamById(16L).orElseThrow(() -> new ResourceNotFoundException("Team not found."));


        //System.out.println(team1);
        //Seeder.teamSkillCalculator(team1, "EUN").getAverageRank();
        //Seeder.teamSkillCalculator(team1, "EUW").getAverageRank();

        //System.out.println(Seeder.teamSkillCalculator(team1, "EUN").getAverageRank());
        //System.out.println(Seeder.teamSkillCalculator(team1, "EUW").getAverageRank());

        Team team3 = teamRepository.findTeamById(17L).orElseThrow(() -> new ResourceNotFoundException("Team not found."));
        Team team4 = teamRepository.findTeamById(18L).orElseThrow(() -> new ResourceNotFoundException("Team not found."));

        List<TeamSkillDto> teamSkillDtoList = new ArrayList<>();

        teamSkillDtoList.add(Seeder.teamSkillCalculator(team1, "EUW"));
        teamSkillDtoList.add(Seeder.teamSkillCalculator(team2, "EUW"));
        teamSkillDtoList.add(Seeder.teamSkillCalculator(team3, "EUW"));
        teamSkillDtoList.add(Seeder.teamSkillCalculator(team4, "EUW"));


       System.out.println(Seeder.seedList(teamSkillDtoList));

       System.out.println(Seeder.seedTournament(tournamentRepository.findById(4L).get()));

    }
}
