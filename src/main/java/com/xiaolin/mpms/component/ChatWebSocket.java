package com.xiaolin.mpms.component;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xiaolin.mpms.entity.chat.ChatMessage;
import com.xiaolin.mpms.entity.VO.ResultVO;
import com.xiaolin.mpms.entity.user.User;
import com.xiaolin.mpms.service.ChatContactService;
import com.xiaolin.mpms.service.ChatMessageService;
import com.xiaolin.mpms.service.UserService;
import com.xiaolin.mpms.utils.RedisCache;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author websocket服务
 */
@ServerEndpoint(value = "/chat/{uid}")
@Component
@Log4j2
public class ChatWebSocket {

    public static UserService userService;

    public static ChatContactService chatContactService;

    public static ChatMessageService chatMessageService;

    public static RedisCache redisCache = new RedisCache();

    /**
     * 记录当前在线连接数
     */
    public static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    private static final Map<String, User> users = new HashMap<>();

    private final List<String> column = new ArrayList<>(Arrays.asList("id", "uid", "username", "nickname", "avatar"));

    List<User> contact = new ArrayList<>();

    final List<Session> contactSession = new ArrayList<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("uid") String uid) {
        sessionMap.put(uid, session);
        String key = "contact:" + uid;
        Boolean hasCache = redisCache.hasKey(key);
        if (!hasCache) {
            contact = chatContactService.getContactList(uid, column);
            redisCache.setCacheList(key, contact);
        } else {
            contact = redisCache.getCacheList(key);
        }
        User userInfo = userService.getUserInfoByUid(uid, column);
        users.put(uid, userInfo);
        List<String> onlineContact = new ArrayList<>();
        for (User user : contact) {
            String contactUid = user.getUid();
            Session onlineSession = sessionMap.get(contactUid);
            if (onlineSession != null) {
                contactSession.add(onlineSession);
                onlineContact.add(contactUid);
            }
        }

        JSONObject data = new JSONObject();
        data.put("type", "user");
        data.put("operation", "list");
        data.put("user", contact);
        data.put("online", onlineContact);
        sendMessage(JSON.toJSONString(ResultVO.success("连接成功", data)), session);
        data.put("type", "user");
        data.put("operation", "add");
        data.put("user", userInfo.getUid());
        data.remove("online");
        sendBatchClientMessage(contactSession, JSON.toJSONString(ResultVO.success("联系人上线", data)));// 后台发送消息给所有联系人的客户端
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("uid") String uid) {
        try (Session ignored = sessionMap.remove(uid)) {
            users.remove(uid);
            JSONObject data = new JSONObject();
            data.put("type", "user");
            data.put("operation", "remove");
            data.put("user", uid);
            sendBatchClientMessage(contactSession, JSON.toJSONString(ResultVO.success("联系人下线", data)));
            log.info("有一连接关闭，移除uid={}的用户session, 当前在线人数为：{}", uid, sessionMap.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     * 后台收到客户端发送过来的消息
     * onMessage 是一个消息的中转站
     * 接受 浏览器端 socket.send 发送过来的 json数据
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("uid") String uid) {
        log.info("服务端收到用户uid={}的消息:{}", uid, message);
        JSONObject obj = JSON.parseObject(message);
        ChatMessage chatMessage = new ChatMessage();
        String toUid = obj.getString("to");
        chatMessage.setFrom(uid);
        chatMessage.setFromUser(users.get(uid));
        chatMessage.setTo(toUid); // to表示发送给哪个用户
        chatMessage.setContent(JSON.toJSONString(obj.getString("content"))); // 发送的消息文本
        chatMessageService.save(chatMessage);
        Session toSession = sessionMap.get(toUid); // 根据 to用户名来获取 session，再通过session发送消息文本
        if (toSession != null) {
            this.sendMessage(JSON.toJSONString(ResultVO.success("消息接收成功", chatMessage)), toSession);
            log.info("发送给用户uid={}，消息：{}", toUid, chatMessage);
        } else {
            log.info("发送失败，未找到用户uid={}的session", toUid);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(String message, Session toSession) {
        try {
            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }

    /**
     * 服务端发送消息给所有客户端
     */
    private void sendAllMessage(String message) {
        try {
            for (Session session : sessionMap.values()) {
                log.info("服务端给客户端[{}]发送消息{}", session.getId(), message);
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }

    /**
     * 服务端批量发送消息给所有客户端
     */
    private void sendBatchClientMessage(List<Session> sessions, String message) {
        try {
            for (Session session : sessions) {
                log.info("服务端给客户端[{}]发送消息{}", session.getId(), message);
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }
}
