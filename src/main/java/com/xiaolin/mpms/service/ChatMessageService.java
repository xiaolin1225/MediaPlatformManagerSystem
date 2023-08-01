/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service;

import com.xiaolin.mpms.entity.chat.ChatMessage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaolin
 * @since 2023-04-01
 */
public interface ChatMessageService extends IService<ChatMessage> {

    List<ChatMessage> getMessageList(String contactUid);
}
