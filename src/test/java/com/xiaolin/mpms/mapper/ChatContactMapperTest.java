/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatContactMapperTest {

    @Autowired
    private ChatContactMapper chatContactMapper;

    @Test
    void selectAllByUid() {
        List<String> column = new ArrayList<>(Arrays.asList("id", "uid", "username", "nickname", "avatar"));
        System.out.println(chatContactMapper.selectListByUid("cadc40815e17448eaa2636cfc5442def",column));
    }
}