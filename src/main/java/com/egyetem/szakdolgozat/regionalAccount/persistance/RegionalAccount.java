package com.egyetem.szakdolgozat.regionalAccount.persistance;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "user_to_riot_region_account", schema = "public")
@NoArgsConstructor
public class RegionalAccount {

    @Id
    @Column(name = "id_user")
    private Integer userId;

    @Column(name = "id_region",nullable = false)
    private String regionId;

    @Column(name = "in_game_name", nullable = false)
    private String inGameName;



}
