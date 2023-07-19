package com.yanhuo.platform.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author:gzh
 * @Date: 2022/4/20 20:27
 * 注意在websocket通信中只能传string
 */
@ServerEndpoint("/api/platform/ws/{userId}")
@Component
@Slf4j
public class WebSocketServer {

     //连接redis
    private static RedisTemplate redisTemplate;

    private static final String USER_KEY = "user:";

    @Resource
    public void setRedisTemplate(RedisTemplate redisTemplate) {

        //取值之前先要序列化
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        this.redisTemplate = redisTemplate;
    }

    /**
     *
     */
    private static final ConcurrentMap<String, Session> SESSION_MAP = new ConcurrentHashMap<>();


    /***
     * 1.把登录用户存到sessionMap中
     * 2.发送给所有人当前登录人员信息
     */
    @OnOpen
    public static void onOpen(Session session, @PathParam("userId") String userId) {

        WebSocketServer.log.info("现在来连接的客户id：" + session.getId() + "用户名：" + userId);

        if (WebSocketServer.SESSION_MAP.containsKey(userId)) {
            WebSocketServer.onClose(WebSocketServer.SESSION_MAP.get(userId));
        }
        WebSocketServer.SESSION_MAP.put(userId, session);
    }

    //关闭连接

    /**
     * 1.把登出的用户从sessionMap中剃除
     * 2.发送给所有人当前登录人员信息
     */
    @OnClose
    public static void onClose(@PathParam("userId") String userId, Session session) throws Exception {
        WebSocketServer.log.info(session.getRequestURI().getPath() + "，关闭连接开始：" + session.getId());

        String userKey = WebSocketServer.USER_KEY +userId;

        if(Boolean.FALSE.equals(WebSocketServer.redisTemplate.hasKey(userKey))){
            WebSocketServer.SESSION_MAP.remove(userId);
            WebSocketServer.log.info(session.getRequestURI().getPath() + "，关闭连接完成：" + session.getId());
        }

    }

    @OnMessage
    public static void onMessage(String message, Session session) {
        WebSocketServer.log.info("前台发送消息：" + message);
    }

    @OnError
    public static void onError(Session session, Throwable error) {
        WebSocketServer.log.error(error.toString());
    }

    private static void onClose(Session session) {
        //判断当前连接是否在线
//        if (!session.isOpen()) {
//            return;
//        }
        try {
            session.close();
        } catch (IOException e) {
            WebSocketServer.log.error("关闭连接异常：" + e);
        }
    }

    public static void sendMessage(String message, Session session) {
        try {
            session.getAsyncRemote().sendText(message);
            WebSocketServer.log.info("推送成功：" + message);
        } catch (Exception e) {
            WebSocketServer.log.error("推送异常：" + e);
        }
    }

    public static void sendMessageTo(String message, String toUserId) {
        try {
            Session session = WebSocketServer.SESSION_MAP.get(toUserId);

            session.getAsyncRemote().sendText(message);
            WebSocketServer.log.info("推送成功：" + message);
        } catch (Exception e) {
            WebSocketServer.log.error("推送异常：" + e);
        }
    }
}
