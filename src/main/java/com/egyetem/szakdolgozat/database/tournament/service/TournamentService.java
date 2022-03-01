package com.egyetem.szakdolgozat.database.tournament.service;

import com.egyetem.szakdolgozat.database.tournament.persistance.Tournament;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeams;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;

import java.util.List;

public interface TournamentService {

    void emailSenderService(Tournament tournament);

    List<List<TournamentToTeams>> currentStandingCalculatorService(Tournament tournament);

    void setCreatorAndSave(Tournament tournament, SiteUser siteUser);

    Tournament getById(Long id);

    boolean validateAndDelete(Tournament tournament, SiteUser siteUser);

    boolean validateAndSaveNewName(Tournament tournament, SiteUser siteUser, String newName);

    boolean validateAndSaveVictor(Tournament tournament, SiteUser siteUser, Long victor);

    boolean validateAndChangeRegion(Tournament tournament, SiteUser siteUser, String region);

    Tournament getByName(String name);

    List<Tournament> getAll();
}
