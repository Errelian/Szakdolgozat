package com.egyetem.szakdolgozat.regionalAccount.persistance;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;


@Data
@NoArgsConstructor
public class RegionalAccountID implements Serializable {

    @Id
    @Column(name = "id_user")
    private Integer userId;

    @Id
    @Column(name = "id_region",nullable = false)
    private String regionId;
}
