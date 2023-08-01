/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springfox.documentation.spring.web.json.Json;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    void getUserListPage() {
    }

    @Test
    void getUserInfoWithPermission() {
    }

    @Test
    void getUserInfo() {
        System.out.println(userService.getUserInfo(1));
    }

    @Test
    void testGetUserInfo() {
    }

    @Test
    void getUserInfoByUid() {
    }

    @Test
    void testGetUserInfoByUid() {
    }

    @Test
    void saveUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void testDeleteUser() {
    }

    @Test
    void isUserExist() {
    }

    @Test
    void login() {
    }

    @Test
    void logout() {
    }

    @Test
    void resetPassword() {
    }
}