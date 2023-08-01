/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service.impl;

import com.xiaolin.mpms.entity.user.AuthUser;
import com.xiaolin.mpms.entity.chat.ChatMessage;
import com.xiaolin.mpms.entity.user.User;
import com.xiaolin.mpms.mapper.ChatMessageMapper;
import com.xiaolin.mpms.service.ChatMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xiaolin
 * @since 2023-04-01
 */
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    private final List<String> column = new ArrayList<>(Arrays.asList("id", "uid", "username", "nickname", "avatar"));

    @Override
    public List<ChatMessage> getMessageList(String contactUid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        User user = authUser.getUser();
//        QueryWrapper<ChatMessage> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("`from`",user.getUid())
//                .eq("`to`",contactUid)
//                .or()
//                .eq("`from`",contactUid)
//                .eq("`to`",user.getUid());
        return chatMessageMapper.getMessageList(user.getUid(),contactUid,column);
    }
}
