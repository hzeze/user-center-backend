package com.hz.usercenterbackend.service;

import java.util.Date;


import com.hz.usercenterbackend.model.domain.User;
import jakarta.annotation.Resource;

import org.apache.ibatis.annotations.Lang;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testSave() {
        User user = new User();
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDelete(0);
        user.setUserStatus(0);
        user.setUsername("heze");
        user.setUserAccount("123");
        user.setUserPassword("123");

        user.setGender(0);
        user.setPhone("123 ");
        user.setEmail("22222");
        boolean a = userService.save(user);

    }

    @Test
    void userRegister() {
        long a = userService.userRegister("heze", "123456789", "123456789");
        Assertions.assertTrue(a > 0);
    }
}