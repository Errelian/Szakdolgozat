package com.egyetem.szakdolgozat.database.tournamentToTeams;


import com.egyetem.szakdolgozat.database.team.persistance.Team;
import com.egyetem.szakdolgozat.database.tournament.persistance.Tournament;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "tournament_to_teams", schema = "public")
public class TournamentToTeams {

    @EmbeddedId
    TournamentToTeamsCKey id;

    @ManyToOne
    @MapsId("teamId")
    @JoinColumn(name = "id_team")
    Team team;

    @ManyToOne
    @MapsId("tournamentId")
    @JoinColumn(name = "id_tournament")
    Tournament tournament;


    @Column(name = "position")
    Integer position;

    @Column(name = "elimination_round")
    Integer eliminationRound;

    public TournamentToTeams(TournamentToTeamsCKey id) {
        this.id = id;
    }

    public TournamentToTeams() {
    }

    public TournamentToTeams(TournamentToTeamsCKey id, Team team,
                             Tournament tournament, Integer position, Integer eliminationRound) {
        this.id = id;
        this.team = team;
        this.tournament = tournament;
        this.position = position;
        this.eliminationRound = eliminationRound;
    }

    @Override
    public String toString() {
        return "TournamentToTeams{" +
            "id=" + id +
            ", position=" + position +
            ", eliminationRound=" + eliminationRound +
            '}';
    }

    public TournamentToTeamsCKey getId() {
        return id;
    }

    public void setId(TournamentToTeamsCKey id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getEliminationRound() {
        return eliminationRound;
    }

    public void setEliminationRound(Integer eliminationRound) {
        this.eliminationRound = eliminationRound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TournamentToTeams that = (TournamentToTeams) o;
        return id.equals(that.id) && position.equals(that.position) &&
            Objects.equals(eliminationRound, that.eliminationRound);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, position, eliminationRound);
    }
}
