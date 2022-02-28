package com.egyetem.szakdolgozat.database.user.service;

import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUserRegisterer;

public interface SiteUserService {

    SiteUser getCurrentlyLoggedInSiteUser();

    SiteUser getArbitraryUser(Long id);

    void modifyNameAndSave(SiteUser siteUser, String name);

    boolean registerUser(SiteUserRegisterer siteUserRegisterer);

    boolean changePassword(SiteUser siteUser, String oldPassword, String newPassword);

    boolean deleteAccount(SiteUser siteUser, String password);
}
