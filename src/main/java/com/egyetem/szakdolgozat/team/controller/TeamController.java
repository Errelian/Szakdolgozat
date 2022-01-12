package com.egyetem.szakdolgozat.team.controller;

import com.egyetem.szakdolgozat.team.persistance.Team;
import com.egyetem.szakdolgozat.team.persistance.TeamRepository;
import com.egyetem.szakdolgozat.user.persistance.SiteUser;
import com.egyetem.szakdolgozat.user.persistance.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
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
public class TeamController {
    TeamRepository teamRepository;
    SiteUserRepository siteUserRepository;

    @Autowired
    public TeamController(TeamRepository teamRepository, SiteUserRepository siteUserRepository) {
        this.teamRepository = teamRepository;
        this.siteUserRepository = siteUserRepository;
    }

    @PostMapping(value = "teams/create", consumes = "application/json")
    public String createNewTeam(@RequestBody Map<String, String> json) {
        if (!json.get("teamName").isBlank()) {
            Team team = new Team(json.get("teamName"));

            teamRepository.save(team);
            return "Successfully created new team.";
        }
        return "Error, team name cannot be blank.";
    }

    @PutMapping(value = "teams/changeName", consumes = "application/json")
    public String changeName(@RequestBody Map<String, String> json) {
        if (!(json.get("teamNameNew").isBlank() || json.get("changerId").isBlank())) {
            Team team = teamRepository.findByTeamName(json.get("teamNameOld"));
            SiteUser changer = siteUserRepository.findUserById(Long.parseLong(json.get("changerId")));

            if (team.getTeamMembers().contains(changer)) {
                team.setTeamName(json.get("teamNameNew"));
                teamRepository.save(team);
                return "Successfully changed team name.";
            }
            return "You are not part of that team";
        }
        return "Error, the teamname or your id cannot be blank";
    }

    @PutMapping(value = "teams/add", consumes = "application/json")
    public String addUser(@RequestBody Map<String, String> json) {
        SiteUser user = siteUserRepository.findUserById(Long.parseLong(json.get("userId")));
        Optional<Team> team = teamRepository.findTeamById(Long.parseLong(json.get("teamId")));

        if (team.isPresent() && user != null) {
            team.get().getTeamMembers().add(user);

            teamRepository.save(team.get());
            return "Successfully added user.";
        }
        return "Some kind of error has occurred.";
    }

    @DeleteMapping(value = "teams/remove", consumes = "application/json")
    public String removeUser(@RequestBody Map<String, String> json) {
        SiteUser user = siteUserRepository.findUserById(Long.parseLong(json.get("userId")));
        Optional<Team> team = teamRepository.findTeamById(Long.parseLong(json.get("teamId")));

        if (team.isPresent() && user != null) {
            if (team.get().getTeamMembers().contains(user)) {
                team.get().getTeamMembers().remove(user);
            } else {
                return "You are not in this team";
            }
            teamRepository.save(team.get());
            return "Successfully removed user.";
        }
        return "Some kind of error has occurred.";
    }

    @GetMapping(value = "teams/all")
    public List<Team> getAllTeams(){
        return teamRepository.findAll();
    }

    @GetMapping(value = "teams/{teamId}")
    public Optional<Team> getTeam(@PathVariable Long teamId){
        return teamRepository.findTeamById(teamId);
    }

}
