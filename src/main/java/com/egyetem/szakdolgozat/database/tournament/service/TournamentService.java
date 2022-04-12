package com.egyetem.szakdolgozat.database.tournament.service;

import com.egyetem.szakdolgozat.database.tournament.persistance.Tournament;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeams;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;
import com.egyetem.szakdolgozat.util.UnauthorizedException;

import java.util.List;

public interface TournamentService {

    void emailSenderService(Tournament tournament);

    List<List<TournamentToTeams>> currentStandingCalculatorService(Tournament tournament);

    void setCreatorAndSave(Tournament tournament, SiteUser siteUser);

    Tournament getById(Long id);

    Tournament getByIdCacheBypass(Long id);

    void validateAndDelete(Tournament tournament, SiteUser siteUser) throws IllegalAccessException;

    Tournament validateAndSaveNewName(Tournament tournament, SiteUser siteUser, String newName)
        throws UnauthorizedException;

    Tournament validateAndSaveVictor(Tournament tournament, SiteUser siteUser, Long victor)
        throws UnauthorizedException;

    Tournament validateAndChangeRegion(Tournament tournament, SiteUser siteUser, String region)
        throws UnauthorizedException;

    Tournament getByName(String name);

    List<Tournament> getAll();

    boolean validate(Tournament tournament);
}
