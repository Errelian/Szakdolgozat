package com.egyetem.szakdolgozat.teamMember.persistance;


import com.egyetem.szakdolgozat.team.persistance.Team;
import com.egyetem.szakdolgozat.user.persistance.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "team_member", schema = "public")
@NoArgsConstructor
public class TeamMember {
    @EmbeddedId
    TeamMemberId id;

    @ManyToOne
    @MapsId("teamId")
    @JoinColumn(name = "id_team")
    Team team;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "id_user")
    User user;
}
