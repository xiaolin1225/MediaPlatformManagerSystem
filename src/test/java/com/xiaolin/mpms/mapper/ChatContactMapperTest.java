/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.mapper;

import com.xiaolin.mpms.entity.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatContactMapperTest {

    @Autowired
    private ChatContactMapper chatContactMapper;

    @Test
    void selectAllByUid() throws IllegalAccessException {
        List<String> column = new ArrayList<>(Arrays.asList("id", "uid", "username", "nickname", "avatar"));
//        System.out.println(chatContactMapper.selectListByUid("cadc40815e17448eaa2636cfc5442def",column));
        Field[] fields = User.class.getDeclaredFields();
        User filterUser = new User();
        User user = new User(1, "111111", "ceshi", "11111", "", "111", "", "", "", null, null, true, 1,1, false, LocalDateTime.now(), LocalDateTime.now(), 1);
        for (Field field : fields) {
            field.setAccessible(true);
            if (column.contains(field.getName())) {
                field.set(filterUser, field.get(user));
            }
        }
        System.out.println(filterUser);
    }
}