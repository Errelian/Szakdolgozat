package com.egyetem.szakdolgozat.teamMember.persistance;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class TeamMemberId implements Serializable {

    @Column(name = "id_team")
    private Integer teamId;

    @Column(name = "id_user")
    private String userId;

}
