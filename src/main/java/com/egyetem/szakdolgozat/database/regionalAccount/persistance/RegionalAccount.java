package com.egyetem.szakdolgozat.database.regionalAccount.persistance;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "user_to_riot_region_account", schema = "public")
@IdClass(RegionalAccountID.class)
public class RegionalAccount {

    @Id
    @Column(name = "id_user")
    private Long userId;

    @Id
    @Column(name = "id_region",nullable = false)
    private String regionId;

    @Column(name = "in_game_name", nullable = false)
    private String inGameName;

    @Column(name = "rank")
    private Short rank;

    public RegionalAccount(Long userId, String regionId, String inGameName) {
        this.userId = userId;
        this.regionId = regionId;
        this.inGameName = inGameName;
    }

    public RegionalAccount() {
    }

    public RegionalAccount(Long userId, String regionId, String inGameName, Short rank) {
        this.userId = userId;
        this.regionId = regionId;
        this.inGameName = inGameName;
        this.rank = rank;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getInGameName() {
        return inGameName;
    }

    public void setInGameName(String inGameName) {
        this.inGameName = inGameName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegionalAccount that = (RegionalAccount) o;
        return userId.equals(that.userId) && regionId.equals(that.regionId) && inGameName.equals(that.inGameName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, regionId, inGameName);
    }

    public Short getRank() {
        return rank;
    }

    public void setRank(Short rank) {
        this.rank = rank;
    }
}
