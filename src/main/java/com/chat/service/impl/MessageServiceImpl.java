package com.chat.service.impl;

import com.chat.dao.MessageMapper;
import com.chat.entity.Message;
import com.chat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    // 保存消息
    @Override
    public boolean saveMessage(Message message) {
        // 如果 createdate 为空，手动设置当前时间
        if (message.getCreatedate() == null) {
            message.setCreatedate(new Date());
        }
        // 使用 MyBatis-Plus 提供的 save 方法进行插入
        return messageMapper.insert(message) > 0;
    }

    // 获取聊天记录
    @Override
    public List<Message> getChatHistory(String user, String friend) {
        return messageMapper.getChatHistory(user, friend);
    }

}
