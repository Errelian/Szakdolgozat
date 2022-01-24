package com.egyetem.szakdolgozat.notify;

import com.egyetem.szakdolgozat.database.regionalAccount.persistance.RegionalAccount;
import com.egyetem.szakdolgozat.database.team.persistance.Team;
import com.egyetem.szakdolgozat.database.tournament.persistance.Tournament;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeams;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Notifier {

    private final static String API_KEY = System.getenv("MAIL_API");

    public static void notifyUsers(Tournament tournament) throws UnirestException {



        List<Team> teams= new ArrayList<>();

        for (TournamentToTeams tournamentToTeams : tournament.getTeams()){
            teams.add(tournamentToTeams.getTeam());
        }

        for (Team team : teams) {
            String message;
            if (team.getId().equals(tournament.getVictorId())){
                message = "Congrats, your teams has won the tournament: " + tournament.getTournamentName();
            }
            else
            {
                message = "The tournament: " + tournament.getTournamentName() + " you partook in has ended. GGWP!";
            }

            for (SiteUser currUser : team.getTeamMembers()) {
                System.out.println(API_KEY);

                System.out.println(currUser.geteMail());
                HttpResponse<String>
                    request = Unirest.post("https://api.eu.mailgun.net/v3/mg.tournament.ga/messages")
                    .basicAuth("api", API_KEY)
                    .queryString("from", "support@tournament.ga")
                    .queryString("to", currUser.geteMail())
                    .queryString("subject", "Tournament ended.")
                    .queryString("text", message)
                    .asString();

            }
        }
    }
}
