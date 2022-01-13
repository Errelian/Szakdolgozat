package com.egyetem.szakdolgozat.regionalAccount.persistance;



import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;


public class RegionalAccountID implements Serializable {

    @Id
    @Column(name = "id_user")
    private Long userId;

    @Id
    @Column(name = "id_region",nullable = false)
    private String regionId;

    public RegionalAccountID(Long userId, String regionId) {
        this.userId = userId;
        this.regionId = regionId;
    }

    public RegionalAccountID() {
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

    @Override
    public String toString() {
        return "RegionalAccountID{" +
            "userId=" + userId +
            ", regionId='" + regionId + '\'' +
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
        RegionalAccountID that = (RegionalAccountID) o;
        return userId.equals(that.userId) && regionId.equals(that.regionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, regionId);
    }
}
