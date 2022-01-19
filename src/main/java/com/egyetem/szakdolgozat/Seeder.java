package com.egyetem.szakdolgozat;

import com.egyetem.szakdolgozat.regionalAccount.persistance.RegionalAccount;
import com.egyetem.szakdolgozat.team.persistance.Team;
import com.egyetem.szakdolgozat.user.persistance.SiteUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Seeder {

    public Seeder() {
    }

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

    public static List<TeamSkillDto> seedList(List<TeamSkillDto> teamSkills) { //TODO ADD STACKOVERFLOW ATTRIBUTION
        if (teamSkills.size() < 2) {
            return new ArrayList<TeamSkillDto>();
        }

        Integer rounds = (int) Math.ceil(Math.log(teamSkills.size()) / Math.log(2));
        double bracketSize = Math.pow(2.0, (double) rounds);
        Integer fakeTeamsRequired = (int) (bracketSize - teamSkills.size());

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

        System.out.println(matches); //TODO CONVERT THIS INTO A LIST<TeamSkillDto>, use sorted list of og list, null is always fake team

        System.out.println(rounds);
        return null;
    }

    private static Integer switchToGhostMatchup(Integer seed , Integer participiantsSize) {

        if (seed <= participiantsSize){
            return seed;
        }
        return null;
    }
}
