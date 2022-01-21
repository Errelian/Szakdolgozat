package com.egyetem.szakdolgozat.database.team.persistance;

import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "team", schema = "public")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_team")
    private Long id;

    @Column(name = "name_team",nullable = false)
    private String teamName;

    @ManyToMany
    @JoinTable(
        name = "team_member",
        joinColumns = @JoinColumn(name="id_team"),
        inverseJoinColumns = @JoinColumn(name="id_user")
    )
    private Set<SiteUser> teamMembers;

    public Team() {
    }

    public Team(Long id, String teamName) {
        this.id = id;
        this.teamName = teamName;
    }

    public Team(String teamName) {
        this.teamName = teamName;
    }

    //@OneToMany(mappedBy = "team")
    //private Set<TournamentToTeams> tournaments;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Team team = (Team) o;
        return id.equals(team.id) && teamName.equals(team.teamName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, teamName);
    }

    @Override
    public String toString() {
        return "Team{" +
            "id=" + id +
            ", teamName='" + teamName + '\'' +
            ", teamMembers=" + teamMembers +
            '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Set<SiteUser> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(Set<SiteUser> teamMembers) {
        this.teamMembers = teamMembers;
    }
}
