package com.danran.user.controller;

import com.danran.common.domain.User;
import com.danran.user.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname UserController
 * @Description TODO
 * @Date 2021/8/9 13:23
 * @Created by ASUS
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/is_valid_user")
    public Boolean isValidUser(@RequestParam("user_id") int userId) {
        return userMapper.selectByPrimaryKey(userId) != null;
    }

    @GetMapping("/get_user_by_id")
    public User getUserById(@RequestParam("user_id") int userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

}
