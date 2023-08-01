/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.mapper;

import com.xiaolin.mpms.entity.user.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoleMapperTest {

    @Autowired
    private RoleMapper roleMapper;
    @Test
    void getRoleInfoById() {
        Role roleInfoById = roleMapper.getRoleInfoById(1);
        System.out.println(roleInfoById);
    }
}