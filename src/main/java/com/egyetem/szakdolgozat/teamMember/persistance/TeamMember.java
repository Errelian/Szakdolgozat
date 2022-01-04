package com.egyetem.szakdolgozat.teamMember.persistance;


import com.egyetem.szakdolgozat.team.persistance.Team;
import com.egyetem.szakdolgozat.user.persistance.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Setter
@Getter
@AllArgsConstructor
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
