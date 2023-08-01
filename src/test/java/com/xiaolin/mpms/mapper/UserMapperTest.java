/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void deleteBatchRoleByUid() {
        List<Integer> ids = Arrays.asList(2, 3);
        System.out.println(userMapper.deleteBatchRoleByUid(ids, 3));
    }

    @Test
    void insertBatchRoleByUid() {
        List<Integer> ids = Arrays.asList(2, 3, 4);
        System.out.println(userMapper.insertBatchRoleByUid(ids, 3));
    }

    @Test
    void generatePassword(){
        System.out.println(passwordEncoder.encode("e10adc3949ba59abbe56e057f20f883e"));
    }
}