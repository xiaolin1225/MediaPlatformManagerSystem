/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service;

import com.xiaolin.mpms.entity.ChatContact;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.mpms.entity.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaolin
 * @since 2023-04-01
 */
public interface ChatContactService extends IService<ChatContact> {
    List<User> getContactList(List<String> column);

    List<User> getContactList(String uid, List<String> column);
}
