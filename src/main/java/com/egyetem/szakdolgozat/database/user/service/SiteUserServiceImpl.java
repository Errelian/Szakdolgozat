package com.egyetem.szakdolgozat.database.user.service;

import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUserRegisterer;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SiteUserServiceImpl implements SiteUserService {

    SiteUserRepository siteUserRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public SiteUserServiceImpl(SiteUserRepository siteUserRepository, PasswordEncoder passwordEncoder) {
        this.siteUserRepository = siteUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SiteUser getCurrentlyLoggedInSiteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return siteUserRepository.findSiteUserByUsername(authentication.getName())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public SiteUser getArbitraryUser(Long id) {
        return siteUserRepository.findUserById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public void modifyNameAndSave(SiteUser siteUser, String name) {
        siteUser.setUsername(name);

        siteUserRepository.save(siteUser);
    }

    @Override
    public boolean registerUser(SiteUserRegisterer siteUserRegisterer) {
        if (!(siteUserRegisterer.getUsername().isBlank() || siteUserRegisterer.getPassword().isBlank() ||
            siteUserRegisterer.geteMail().isBlank())) {

            SiteUser user =
                new SiteUser(siteUserRegisterer.getUsername(), passwordEncoder.encode(siteUserRegisterer.getPassword()),
                    siteUserRegisterer.geteMail());

            siteUserRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean changePassword(SiteUser siteUser, String oldPassword, String newPassword) {
        if (passwordEncoder.matches(oldPassword, siteUser.getPassword())) {
            siteUser.setPassword(passwordEncoder.encode(newPassword));
            siteUserRepository.save(siteUser);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteAccount(SiteUser siteUser, String password) {
        if (passwordEncoder.matches(password, siteUser.getPassword())) {
            siteUserRepository.delete(siteUser);
            SecurityContextHolder.getContext().setAuthentication(null);
            return true;
        }
        return false;
    }
}
