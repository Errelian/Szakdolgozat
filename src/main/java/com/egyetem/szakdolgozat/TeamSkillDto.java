package com.egyetem.szakdolgozat;

import com.egyetem.szakdolgozat.team.persistance.Team;

import java.util.Objects;

public class TeamSkillDto {

    private Long id;

    private String name;

    private Double averageRank;

    private Boolean fakeTeam;

    public TeamSkillDto(Team team, Double averageRank, Boolean fakeTeam) {
        this.id = team.getId();
        this.name = team.getTeamName();
        this.averageRank = averageRank;
        this.fakeTeam = fakeTeam;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAverageRank() {
        return averageRank;
    }

    public void setAverageRank(Double averageRank) {
        this.averageRank = averageRank;
    }

    @Override
    public String toString() {
        return "TeamSkillDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", averageRank=" + averageRank +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TeamSkillDto that = (TeamSkillDto) o;
        return id.equals(that.id) && name.equals(that.name) && averageRank.equals(that.averageRank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, averageRank);
    }

    public Boolean getFakeTeam() {
        return fakeTeam;
    }

    public void setFakeTeam(Boolean fakeTeam) {
        this.fakeTeam = fakeTeam;
    }
}
