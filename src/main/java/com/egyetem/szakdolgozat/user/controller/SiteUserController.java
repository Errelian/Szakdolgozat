package com.egyetem.szakdolgozat.user.controller;

import com.egyetem.szakdolgozat.region.persistance.RegionRepository;
import com.egyetem.szakdolgozat.regionalAccount.persistance.RegionalAccount;
import com.egyetem.szakdolgozat.team.persistance.Team;
import com.egyetem.szakdolgozat.team.persistance.TeamRepository;
import com.egyetem.szakdolgozat.tournament.persistance.Tournament;
import com.egyetem.szakdolgozat.tournament.persistance.TournamentRepository;
import com.egyetem.szakdolgozat.tournamentToTeams.persistance.TournamentToTeamsRepository;
import com.egyetem.szakdolgozat.user.persistance.SiteUser;
import com.egyetem.szakdolgozat.user.persistance.SiteUserRegisterer;
import com.egyetem.szakdolgozat.user.persistance.SiteUserRepository;
import com.egyetem.szakdolgozat.user.persistance.SiteUserUsernameChanger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class SiteUserController {

    SiteUserRepository siteUserRepository;
    RegionRepository regionRepository;
    TeamRepository teamRepository;
    TournamentRepository tournamentRepository;
    TournamentToTeamsRepository tournamentToTeamsRepository;

    @Autowired
    public SiteUserController(SiteUserRepository siteUserRepository, RegionRepository regionRepository, TeamRepository teamRepository, TournamentRepository tournamentRepository, TournamentToTeamsRepository tournamentToTeamsRepository){
        this.siteUserRepository = siteUserRepository;
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

    @GetMapping(value = "/users/{userId}") //TODO REMOVE PASSWORD FROM THIS IN A PRETTY WAY
    public SiteUser getSiteUserInfo(@PathVariable Long userId){
        return siteUserRepository.findUserById(userId);
    }

    @GetMapping(value = "/users/{userId}/accounts")
    public Set<RegionalAccount> getUsersAccounts(@PathVariable Long userId){
        return siteUserRepository.findUserById(userId).getRegionalAccounts();
    }

    @GetMapping(value = "/users/{userId}/teams")
    public Set<Team> getUserTeams(@PathVariable Long userId){
        return teamRepository.findByTeamMembers_Id(userId);
    }

    @PutMapping(value = "/users/changeName", consumes = "application/json")
    public void changeUsername(@RequestBody SiteUserUsernameChanger siteUserUsernameChanger){
        SiteUser tempUser = siteUserRepository.findUserById(siteUserUsernameChanger.getUserId());

        tempUser.setUsername(siteUserUsernameChanger.getNewName());

        siteUserRepository.save(tempUser);
    }

    @PostMapping(value = "/users/register", consumes = "application/json")
    public void registerUser(@RequestBody SiteUserRegisterer registerUser){
        SiteUser user = new SiteUser(registerUser.getUsername(), registerUser.getPassword(),registerUser.geteMail());

        siteUserRepository.save(user);
    }

    @GetMapping(value = "/users/{userId}/password")
    public void testMethod(@PathVariable Long userId){
        System.out.println(siteUserRepository.findUserById(userId).getPassword());
    }
}
