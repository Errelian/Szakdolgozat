package com.egyetem.szakdolgozat.seeding;

import com.egyetem.szakdolgozat.database.team.persistance.Team;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SeederTest {

    @Test
    void seedListTestWhenThereAreEnoughTeams() {
        List<TeamSkillDto> teamSkillDtoList= new ArrayList<>();
        Team team = new Team(1L, "test", 1L);
        teamSkillDtoList.add(new TeamSkillDto(team, 5.0, false));
        teamSkillDtoList.add(new TeamSkillDto(team, 5.1, false));
        teamSkillDtoList.add(new TeamSkillDto(team, 4.0, false));
        teamSkillDtoList.add(new TeamSkillDto(team, 6.0, false));


        var matchups = Seeder.seedList(teamSkillDtoList);

        List<ArrayList<TeamSkillDto>> expected = new ArrayList<>();


        var list1 = new ArrayList<TeamSkillDto>();
        list1.add(new TeamSkillDto(team, 6.0, false));
        list1.add(new TeamSkillDto(team, 4.0, false));
        expected.add(list1);

        var list2 = new ArrayList<TeamSkillDto>();
        list2.add(new TeamSkillDto(team, 5.0, false));
        list2.add(new TeamSkillDto(team, 5.1, false));
        expected.add(list2);

        assertIterableEquals(matchups, expected);
    }

    @Test
    void seedListTestWhenThereAreNotEnoughTeams() {
        List<TeamSkillDto> teamSkillDtoList= new ArrayList<>();
        Team team = new Team(1L, "test", 1L);
        teamSkillDtoList.add(new TeamSkillDto(team, 5.0, false));
        teamSkillDtoList.add(new TeamSkillDto(team, 5.1, false));
        teamSkillDtoList.add(new TeamSkillDto(team, 4.0, false));


        var matchups = Seeder.seedList(teamSkillDtoList);

        List<ArrayList<TeamSkillDto>> expected = new ArrayList<>();


        var list1 = new ArrayList<TeamSkillDto>();
        list1.add(new TeamSkillDto(team, 5.1, false));
        list1.add(new TeamSkillDto(new Team(-1L, "fake", 1L), 0.0, true));
        expected.add(list1);

        var list2 = new ArrayList<TeamSkillDto>();
        list2.add(new TeamSkillDto(team, 4.0, false));
        list2.add(new TeamSkillDto(team, 5.0, false));
        expected.add(list2);

        assertIterableEquals(matchups, expected);



    }
}