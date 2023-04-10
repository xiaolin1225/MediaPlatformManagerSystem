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
class ChatMessageMapperTest {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    private final List<String> column = new ArrayList<>(Arrays.asList("id", "uid", "username", "nickname", "avatar"));

    @Test
    void getMessageList() {
        System.out.println(chatMessageMapper.getMessageList("3ea7f3139e114035ae723c0a66d5118e","cadc40815e17448eaa2636cfc5442def",column));
    }
}