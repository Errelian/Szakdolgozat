package com.egyetem.szakdolgozat.user.controller;

import com.egyetem.szakdolgozat.region.persistance.Region;
import com.egyetem.szakdolgozat.region.persistance.RegionRepository;
import com.egyetem.szakdolgozat.team.persistance.TeamRepository;
import com.egyetem.szakdolgozat.user.persistance.User;
import com.egyetem.szakdolgozat.user.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    UserRepository userRepository;
    RegionRepository regionRepository;
    TeamRepository teamRepository;

    @Autowired
    public UserController(UserRepository userRepository, RegionRepository regionRepository, TeamRepository teamRepository){
        this.userRepository = userRepository;
        this.regionRepository = regionRepository;
        this.teamRepository = teamRepository;
    }

    @RequestMapping("/test1")
    public String testFindAll(){
        return userRepository.findAll().toString();
    }
}
