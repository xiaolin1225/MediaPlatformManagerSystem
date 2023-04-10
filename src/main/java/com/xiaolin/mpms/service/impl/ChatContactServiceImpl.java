/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service.impl;

import com.xiaolin.mpms.entity.AuthUser;
import com.xiaolin.mpms.entity.ChatContact;
import com.xiaolin.mpms.entity.User;
import com.xiaolin.mpms.mapper.ChatContactMapper;
import com.xiaolin.mpms.service.ChatContactService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaolin
 * @since 2023-04-01
 */
@Service
public class ChatContactServiceImpl extends ServiceImpl<ChatContactMapper, ChatContact> implements ChatContactService {

    @Autowired
    private ChatContactMapper chatContactMapper;

    @Override
    public List<User> getContactList(List<String> column) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        User user = authUser.getUser();
        return chatContactMapper.selectListByUid(user.getUid(),column);
    }

    @Override
    public List<User> getContactList(String uid, List<String> column) {
        return chatContactMapper.selectListByUid(uid,column);
    }
}
