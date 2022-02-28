package com.egyetem.szakdolgozat.database.team.service;

import com.egyetem.szakdolgozat.database.team.persistance.Team;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;

import java.util.List;

public interface TeamService {

    boolean validateAndCreateNewTeam(String name, SiteUser siteUser);

    Team getTeamByName(String name);

    Team getTeamById(Long id);

    boolean validateAndChangeName(Team team, SiteUser siteUser, String newName);

    boolean deleteTeam(Team team, SiteUser siteUser);

    void addUserToTeam(Team team, SiteUser siteUser);

    boolean removeUserFromTeam(Team team, SiteUser siteUser);

    List<Team> findAllTeams();
}
