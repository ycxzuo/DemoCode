package com.yczuoxin.others.spring.controller;

import com.yczuoxin.others.spring.bean.User;
import com.yczuoxin.others.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public String testAop(){
        User user = new User();
        user.setId(1L);
        user.setName("好好好");
        userService.save(user);
        return "success";
    }
}
