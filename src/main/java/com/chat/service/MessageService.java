package com.chat.service;

import com.chat.entity.Message;

import java.util.List;

public interface MessageService {

    boolean saveMessage(Message message);
    List<Message> getChatHistory(String user, String friend);
}
