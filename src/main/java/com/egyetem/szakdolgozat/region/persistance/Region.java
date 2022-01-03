package com.egyetem.szakdolgozat.region.persistance;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "region", schema = "public")
@NoArgsConstructor
public class Region {

    @Id
    @Column(name = "id_region")
    private String regionId;

}
