package com.egyetem.szakdolgozat.user.persistance;

import com.egyetem.szakdolgozat.regionalAccount.persistance.RegionalAccount;
import com.egyetem.szakdolgozat.team.persistance.Team;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "site_user", schema = "public")
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    @Column(name = "username",nullable = false)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password",nullable = false)
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "e_mail", nullable = false)
    private String eMail;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    private Set<RegionalAccount> regionalAccounts;

    @ManyToMany(mappedBy = "teamMembers", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Team> userTeams;

    public SiteUser(String username, String password, String eMail) {
        this.username = username;
        this.password = password;
        this.eMail = eMail;
    }

    public SiteUser(Long id, String username, String password, String eMail) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.eMail = eMail;
    }

    public SiteUser() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SiteUser siteUser = (SiteUser) o;
        return id.equals(siteUser.id) && username.equals(siteUser.username) && password.equals(siteUser.password) &&
            eMail.equals(siteUser.eMail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, eMail);
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", eMail='" + eMail + '\'' +
            ", regionalAccounts=" + regionalAccounts +
            '}';
    }

    public Optional<RegionalAccount> getRegionalAccountByRegion(String region){
        for (RegionalAccount regionalAccount : regionalAccounts){
            if (regionalAccount.getRegionId().equals(region)){
                return Optional.of(regionalAccount);
            }
        }
        return Optional.empty();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public Set<RegionalAccount> getRegionalAccounts() {
        return regionalAccounts;
    }

    public void setRegionalAccounts(
        Set<RegionalAccount> regionalAccounts) {
        this.regionalAccounts = regionalAccounts;
    }

    public Set<Team> getUserTeams() {
        return userTeams;
    }

    public void setUserTeams(Set<Team> userTeams) {
        this.userTeams = userTeams;
    }
}
