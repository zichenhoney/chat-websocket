package com.chat.websocket;

import com.chat.entity.Message;
import com.chat.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private static final Map<String, String> userSessions = new ConcurrentHashMap<>(); // 存储用户与 session 的映射

    @Autowired
    private MessageService messageService;  // 注入服务

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String user = getUserFromSession(session);  // 获取用户名（通过 URI 或认证）
        sessions.put(user, session);
        userSessions.put(session.getId(), user); // 将用户与 session 绑定
        System.out.println("WebSocket连接建立，用户：" + user);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        // 使用 JSON 工具解析前端发来的 JSON 数据
        ObjectMapper objectMapper = new ObjectMapper();
        Message msg = objectMapper.readValue(payload, Message.class);

        // 设置创建时间（如果前端没传的话）
        if (msg.getCreatedate() == null) {
            msg.setCreatedate(new Date());
        }

        // 设置状态为未读（0）
        if (msg.getStatus() == null) {
            msg.setStatus("0");
        }

        // 存入数据库
        boolean saved = messageService.saveMessage(msg);
        if (!saved) {
            session.sendMessage(new TextMessage("保存消息失败"));
            return;
        }

        // 转发给接收者
        WebSocketSession receiverSession = sessions.get(msg.getReceiveuser());
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(payload));
        }

        // 给自己也回显（前端显示发送成功）
        session.sendMessage(new TextMessage(payload));
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String user = userSessions.get(session.getId());
        sessions.remove(user);
        userSessions.remove(session.getId());
        System.out.println("WebSocket连接关闭，用户：" + user);
    }

    private String getUserFromSession(WebSocketSession session) {
        // 这里假设你从 session 或 URI 中获取用户信息
        return (String) session.getAttributes().get("username");
    }

    private Message parseMessage(String payload) {
        // 假设消息是 JSON 格式，使用 JSON 解析器解析成 Message 对象
        // 示例：返回解析后的 Message 对象
        // 这里的解析需要根据你的消息格式来调整
        return new Message();  // 这是一个简化示例，实际需要用 JSON 解析
    }
}
