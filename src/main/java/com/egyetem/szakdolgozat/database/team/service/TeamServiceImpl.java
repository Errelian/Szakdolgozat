package com.egyetem.szakdolgozat.database.team.service;

import com.egyetem.szakdolgozat.database.team.persistance.Team;
import com.egyetem.szakdolgozat.database.team.persistance.TeamRepository;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TeamServiceImpl implements  TeamService{

    TeamRepository teamRepository;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public boolean validateAndCreateNewTeam(String name, SiteUser siteUser) {
        if (!name.isBlank()) {
            Team team = new Team(name, siteUser.getId());

            Set<SiteUser> teamMembers = new HashSet<>();
            teamMembers.add(siteUser);

            team.setTeamMembers(teamMembers);

            teamRepository.save(team);
            return true;
        }
        return false;
    }

    @Override
    public Team getTeamByName(String name) {
        return teamRepository.findByTeamName(name).orElseThrow(() -> new ResourceNotFoundException("Team not found."));
    }

    @Override
    public Team getTeamById(Long id) {
        return teamRepository.findTeamById(id).orElseThrow(() -> new ResourceNotFoundException("Team not found."));
    }

    @Override
    public boolean validateAndChangeName(Team team, SiteUser siteUser, String newName) {
        if (team.getCreatorId().equals(siteUser.getId())) {
            team.setTeamName(newName);
            teamRepository.save(team);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteTeam(Team team, SiteUser siteUser) {
        if (team.getCreatorId().equals(siteUser.getId())) {
            teamRepository.delete(team);
            return true;
        }
        return false;
    }

    @Override
    public void addUserToTeam(Team team, SiteUser siteUser) {
        team.getTeamMembers().add(siteUser);
        teamRepository.save(team);
    }

    @Override
    public boolean removeUserFromTeam(Team team, SiteUser siteUser) {
        if (team.getTeamMembers().contains(siteUser)) {
            team.getTeamMembers().remove(siteUser);
        } else {
            return false;
        }
        teamRepository.save(team);
        return true;
    }

    @Override
    public List<Team> findAllTeams() {
        return teamRepository.findAll();
    }
}
