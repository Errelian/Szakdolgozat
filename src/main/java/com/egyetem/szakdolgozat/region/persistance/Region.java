package com.egyetem.szakdolgozat.region.persistance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "region", schema = "public")
public class Region {

    @Id
    @Column(name = "id_region")
    private String regionId;

    public Region(String regionId) {
        this.regionId = regionId;
    }

    public Region() {
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Region region = (Region) o;
        return regionId.equals(region.regionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regionId);
    }

    @Override
    public String toString() {
        return "Region{" +
            "regionId='" + regionId + '\'' +
            '}';
    }
}
