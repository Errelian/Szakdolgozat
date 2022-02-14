package com.egyetem.szakdolgozat.database.tournament.service;

import com.egyetem.szakdolgozat.database.tournament.persistance.Tournament;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeams;

import java.util.List;

public interface TournamentService {

    void emailSenderService(Tournament tournament);

    List<List<TournamentToTeams>> currentStandingCalculatorService(Tournament tournament);
}
