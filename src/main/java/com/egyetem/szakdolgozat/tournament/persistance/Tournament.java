package com.egyetem.szakdolgozat.tournament.persistance;

import com.egyetem.szakdolgozat.tournamentToTeams.persistance.TournamentToTeams;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tournament", schema = "public")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tournament")
    private Long id;

    @Column(name = "name_tournament",nullable = false)
    private String tournamentName;

    @Column(name = "start_date_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @Column(name = "id_victor")
    private Long victorId;

    @Column(name = "id_region")
    private String regionId;

    @Column(name = "id_creator")
    private Long creatorId;

    @OneToMany(mappedBy = "tournament")
    @JsonIgnore
    private List<TournamentToTeams> teams;

    public Tournament() {
    }

    public Tournament(Long id, String tournamentName, LocalDateTime startTime, Long victorId, String regionId,
                      Long creatorId) {
        this.id = id;
        this.tournamentName = tournamentName;
        this.startTime = startTime;
        this.victorId = victorId;
        this.regionId = regionId;
        this.creatorId = creatorId;
    }

    public Tournament(Long id, String tournamentName, LocalDateTime startTime, String regionId, Long creatorId) {
        this.id = id;
        this.tournamentName = tournamentName;
        this.startTime = startTime;
        this.regionId = regionId;
        this.creatorId = creatorId;
    }

    public Long getId() {
        return id;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Long getVictorId() {
        return victorId;
    }

    public String getRegionId() {
        return regionId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setVictorId(Long victorId) {
        this.victorId = victorId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tournament that = (Tournament) o;
        return id.equals(that.id) && tournamentName.equals(that.tournamentName) &&
            Objects.equals(startTime, that.startTime) && regionId.equals(that.regionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tournamentName, startTime, regionId);
    }

    @Override
    public String toString() {
        return "Tournament{" +
            "id=" + id +
            ", tournamentName='" + tournamentName + '\'' +
            ", startTime=" + startTime +
            ", victorId=" + victorId +
            ", regionId=" + regionId +
            ", creatorId=" + creatorId +
            '}';
    }

    public List<TournamentToTeams> getTeams() {
        return teams;
    }

    public void setTeams(List<TournamentToTeams> teams) {
        this.teams = teams;
    }
}
