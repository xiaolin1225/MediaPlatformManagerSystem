/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.xiaolin.mpms.entity.ChatMessage;
import com.xiaolin.mpms.entity.ResultVO;
import com.xiaolin.mpms.entity.User;
import com.xiaolin.mpms.service.ChatContactService;
import com.xiaolin.mpms.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("chat")
public class ChatController {

    @Autowired
    private ChatContactService chatContactService;

    @Autowired
    private ChatMessageService chatMessageService;

    private final List<String> column = new ArrayList<>(Arrays.asList("id", "uid", "username", "nickname", "avatar"));

    @GetMapping("contact")
    public ResultVO<List<User>> getContactList(){
        return ResultVO.success("数据获取成功",chatContactService.getContactList(column));
    }

    @GetMapping("message")
    public ResultVO<List<ChatMessage>> getMessage(String uid){
        return ResultVO.success("数据获取成功",chatMessageService.getMessageList(uid));
    }
}
