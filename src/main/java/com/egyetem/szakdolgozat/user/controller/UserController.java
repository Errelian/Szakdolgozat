package com.egyetem.szakdolgozat.user.controller;

import com.egyetem.szakdolgozat.region.persistance.RegionRepository;
import com.egyetem.szakdolgozat.team.persistance.Team;
import com.egyetem.szakdolgozat.team.persistance.TeamRepository;
import com.egyetem.szakdolgozat.tournament.persistance.Tournament;
import com.egyetem.szakdolgozat.tournament.persistance.TournamentRepository;
import com.egyetem.szakdolgozat.tournamentToTeams.persistance.TournamentToTeams;
import com.egyetem.szakdolgozat.tournamentToTeams.persistance.TournamentToTeamsRepository;
import com.egyetem.szakdolgozat.user.persistance.User;
import com.egyetem.szakdolgozat.user.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class UserController {

    UserRepository userRepository;
    RegionRepository regionRepository;
    TeamRepository teamRepository;
    TournamentRepository tournamentRepository;
    TournamentToTeamsRepository tournamentToTeamsRepository;

    @Autowired
    public UserController(UserRepository userRepository, RegionRepository regionRepository, TeamRepository teamRepository, TournamentRepository tournamentRepository, TournamentToTeamsRepository tournamentToTeamsRepository){
        this.userRepository = userRepository;
        this.regionRepository = regionRepository;
        this.teamRepository = teamRepository;
        this.tournamentRepository = tournamentRepository;
        this.tournamentToTeamsRepository = tournamentToTeamsRepository;
    }

    @RequestMapping("/test1")
    public String testFindAll(){

        //userRepository.save(new User("SaveTestPista", "someKindOfHash", "saving@save.com"));

        /*List<User> userList = userRepository.findAll();
        String test = "";

        for (User currUser : userList){

            Set<Team> userTeam =  currUser.getUserTeams();

            for (Team currTeam : userTeam){
                test = test + currTeam.getTeamName() + "\n";
            }
        }*/

        //List<TournamentToTeams> tournamentList = tournamentToTeamsRepository.findAll();

        List<Tournament> tournamentList = tournamentRepository.findAll();

        return tournamentList.get(0).getTeams().toString();
    }
}
