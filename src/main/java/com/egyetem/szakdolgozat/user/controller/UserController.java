package com.egyetem.szakdolgozat.user.controller;

import com.egyetem.szakdolgozat.region.persistance.Region;
import com.egyetem.szakdolgozat.region.persistance.RegionRepository;
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

    @Autowired
    public UserController(UserRepository userRepository, RegionRepository regionRepository){
        this.userRepository = userRepository;
        this.regionRepository = regionRepository;
    }

    @RequestMapping("/test1")
    public String testFindAll(){
        List<User> firstTestList =  userRepository.findAll();

        return firstTestList.toString();
    }
}
