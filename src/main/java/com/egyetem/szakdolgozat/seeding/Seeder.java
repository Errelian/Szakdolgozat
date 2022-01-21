package com.egyetem.szakdolgozat.seeding;

import com.egyetem.szakdolgozat.database.regionalAccount.persistance.RegionalAccount;
import com.egyetem.szakdolgozat.database.team.persistance.Team;
import com.egyetem.szakdolgozat.database.tournament.persistance.Tournament;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeams;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Seeder {


    public static TeamSkillDto teamSkillCalculator(Team team, String region) {

        Set<SiteUser> siteUsers = team.getTeamMembers();

        Double skillSum = 0.0;
        Double memberCount = 0.0;

        if (siteUsers.isEmpty()) {
            return new TeamSkillDto(team, 0.0, true); //a team without members always loses theoretically
        }
        for (SiteUser user : siteUsers) {
            Optional<RegionalAccount> regionalAccount = user.getRegionalAccountByRegion(region);
            if (regionalAccount.isPresent()) {
                memberCount++;
                if (regionalAccount.get().getRank() != null) {
                    skillSum += regionalAccount.get().getRank();
                } else {
                    skillSum += 3;
                }
            } else { //Gold is considered an average rank in League of Legends, so if no rank is found it defaults to it.
                memberCount++;
                skillSum += 3;
            }
        }
        return new TeamSkillDto(team, skillSum / memberCount, false);
    }

    public static ArrayList<ArrayList<TeamSkillDto>> seedList(List<TeamSkillDto> teamSkills) { //TODO ADD STACKOVERFLOW ATTRIBUTION
        if (teamSkills.size() < 2) {
            return new ArrayList<>();
        }

        int rounds = (int) Math.ceil(Math.log(teamSkills.size()) / Math.log(2));

        List<ArrayList<Integer>> matches = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> initial = new ArrayList<>();

        initial.add(1);
        initial.add(2);
        matches.add(initial);

        for (int currRound = 1; currRound < rounds; currRound++) {
            List<ArrayList<Integer>> thisRoundMatches = new ArrayList<>();
            int sum = (int) Math.pow(2, currRound + 1) + 1; //SUS

            for (ArrayList<Integer> match : matches) {
                Integer first = switchToGhostMatchup(match.get(0), teamSkills.size());
                Integer second = switchToGhostMatchup(sum - match.get(0), teamSkills.size());

                ArrayList<Integer> temporaryList = new ArrayList<>();
                temporaryList.add(first);
                temporaryList.add(second);
                thisRoundMatches.add(temporaryList);

                first = switchToGhostMatchup(sum - match.get(1), teamSkills.size());
                second = switchToGhostMatchup(match.get(1), teamSkills.size());

                ArrayList<Integer> temporaryList2 = new ArrayList<>();
                temporaryList2.add(first);
                temporaryList2.add(second);
                thisRoundMatches.add(temporaryList2);

            }
            matches = thisRoundMatches;
        }

        teamSkills.sort((Comparator.comparing(TeamSkillDto::getAverageRank)));
        Collections.reverse(teamSkills);
        ArrayList<ArrayList<TeamSkillDto>> teamSkillMatches = new ArrayList<>();


        for (ArrayList<Integer> match : matches){
            ArrayList<TeamSkillDto> matchup = new ArrayList<>();
            for (Integer seed : match){
                if (seed != null){
                    matchup.add(teamSkills.get(seed - 1));
                }
                else{
                    matchup.add(new TeamSkillDto(new Team(-1L, "fake"), 0.0, true));
                }
            }
            teamSkillMatches.add(matchup);
        }

        return teamSkillMatches;
    }

    public static ArrayList<ArrayList<TeamSkillDto>> seedTournament(Tournament tournament){
        List<TournamentToTeams> teams = tournament.getTeams();

        List<TeamSkillDto> teamSkills = new ArrayList<>();

        for (TournamentToTeams teamTable : teams){
            teamSkills.add(Seeder.teamSkillCalculator(teamTable.getTeam(), tournament.getRegionId()));
        }

        return Seeder.seedList(teamSkills);
    }

    private static Integer switchToGhostMatchup(Integer seed , Integer participiantsSize) {

        if (seed <= participiantsSize){
            return seed;
        }
        return null;
    }
}
