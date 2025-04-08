package com.chat.controller;

import com.chat.entity.Message;
import com.chat.service.MessageService;
import com.chat.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class MessageController {

    @Autowired
    private MessageService messageService;


    // 获取聊天记录
    @GetMapping("/history")
    public ResultVO getChatHistory(@RequestParam String user, @RequestParam String friend) {
        List<Message> messages = messageService.getChatHistory(user, friend);
        return new ResultVO(200, "查询成功", true, messages, 0);
    }
}
