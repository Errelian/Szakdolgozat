package com.egyetem.szakdolgozat.tournamentToTeams.persistance;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TournamentToTeamsCKey implements Serializable {

    @Column(name = "id_tournament")
    Long teamId;

    @Column(name = "id_team")
    Long tournamentId;

    public TournamentToTeamsCKey(Long teamId, Long tournamentId) {
        this.teamId = teamId;
        this.tournamentId = tournamentId;
    }

    public TournamentToTeamsCKey() {
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TournamentToTeamsCKey that = (TournamentToTeamsCKey) o;
        return teamId.equals(that.teamId) && tournamentId.equals(that.tournamentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamId, tournamentId);
    }

    @Override
    public String toString() {
        return "TournamentToTeamsCKey{" +
            "teamId=" + teamId +
            ", tournamentId=" + tournamentId +
            '}';
    }
}
