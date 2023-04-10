/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.config;

import com.xiaolin.mpms.component.ChatWebSocket;
import com.xiaolin.mpms.service.ChatContactService;
import com.xiaolin.mpms.service.ChatMessageService;
import com.xiaolin.mpms.service.UserService;
import com.xiaolin.mpms.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Autowired
    public void userService(UserService userService) {
        ChatWebSocket.userService = userService;
    }

    @Autowired
    private void chatContactService(ChatContactService chatContactService){ChatWebSocket.chatContactService = chatContactService;}

    @Autowired
    private void chatMessageService(ChatMessageService chatMessageService){ChatWebSocket.chatMessageService = chatMessageService;}

    @Autowired
    private void redisCache(RedisCache redisCache){ChatWebSocket.redisCache = redisCache;}
}
