package com.chat.websocket;

import com.chat.entity.Message;
import com.chat.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    // 存储在线用户会话，key 为 username
    private static final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Autowired
    private MessageService messageService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 从拦截器传入的 attributes 中获取用户名
        String username = (String) session.getAttributes().get("username");
        if (username == null || username.trim().isEmpty()) {
            System.err.println("No username found in WebSocket session attributes. Closing connection.");
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("No username, please log in."));
            return;
        }
        sessions.put(username, session);
        System.out.println("WebSocket connection established for user: " + username);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        Message msg;
        try {
            msg = objectMapper.readValue(payload, Message.class);
        } catch (Exception e) {
            String errorMsg = objectMapper.writeValueAsString(
                    new MessageResponse("系统", "消息格式错误", new Date())
            );
            session.sendMessage(new TextMessage(errorMsg));
            return;
        }

        if (msg.getCreatedate() == null) {
            msg.setCreatedate(new Date());
        }
        if (msg.getStatus() == null) {
            msg.setStatus("0");
        }

        boolean saved = messageService.saveMessage(msg);
        if (!saved) {
            String errorMsg = objectMapper.writeValueAsString(
                    new MessageResponse("系统", "保存消息失败", new Date())
            );
            session.sendMessage(new TextMessage(errorMsg));
            return;
        }

        // 将消息转换为 JSON 字符串
        String jsonMsg = objectMapper.writeValueAsString(msg);
        // 转发给接收者
        WebSocketSession receiverSession = sessions.get(msg.getReceiveuser());
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(jsonMsg));
        } else {
            String offlineMsg = objectMapper.writeValueAsString(
                    new MessageResponse("系统", "对方不在线", new Date())
            );
            session.sendMessage(new TextMessage(offlineMsg));
        }
        // 可选：也给发送者回显
        session.sendMessage(new TextMessage(jsonMsg));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String username = (String) session.getAttributes().get("username");
        sessions.remove(username);
        System.out.println("WebSocket connection closed for user: " + username + ", reason: " + status);
    }

    // 用于返回系统消息的内部类（简单实现）
    @Getter
    public static class MessageResponse {
        // getters and setters
        private String senduser;
        private String detail;
        private Date createdate;

        public MessageResponse(String senduser, String detail, Date createdate) {
            this.senduser = senduser;
            this.detail = detail;
            this.createdate = createdate;
        }

        public void setSenduser(String senduser) { this.senduser = senduser; }

        public void setDetail(String detail) { this.detail = detail; }

        public void setCreatedate(Date createdate) { this.createdate = createdate; }
    }
}
