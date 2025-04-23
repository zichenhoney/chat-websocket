package com.chat.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chat.entity.Users;
import com.chat.service.FriendsService;
import com.chat.service.UsersService;
import com.chat.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/friends")
public class FriendsController {

    @Autowired
    private FriendsService friendsService;

    @Autowired
    private UsersService usersService;

    @PostMapping("/add")
    public ResultVO addFriend(@RequestBody Map<String, String> request) {
        String userName = request.get("user_name");
        String friendName = request.get("friend_name");

        System.out.println("当前用户（请求参数）：" + userName);
        System.out.println("要添加的好友：" + friendName);



        if (userName.equals(friendName)) {
            return new ResultVO(400, "不能添加自己为好友", false, null, 0);
        }

        Users friend = usersService.getOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Users>()
                        .eq("username", friendName)
        );
        if (friend == null) {
            return new ResultVO(400, "好友不存在", false, null, 0);
        }

        if (friendsService.existsFriendship(userName, friendName)) {
            return new ResultVO(400, "你们已经是好友", false, null, 0);
        }

        // 使用事务控制的双向好友添加
        friendsService.addFriend(userName, friendName);

        return new ResultVO(200, "好友添加成功", true, null, 0);
    }



    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getFriendsList(@RequestParam String user_name) {
        System.out.println("查询好友列表，当前用户：" + user_name);
        // 返回好友名称的列表
        List<String> friendNames = friendsService.getFriendsByUsername(user_name);
        // 存放好友详细信息
        List<Map<String, String>> friendsInfo = new ArrayList<>();
        for (String friendName : friendNames) {
            // 根据好友名称查询用户详细信息
            Users friend = usersService.getOne(new QueryWrapper<Users>().eq("username", friendName));
            if (friend != null) {
                Map<String, String> info = new HashMap<>();
                info.put("name", friend.getUsername());
                info.put("avatar", friend.getAvatar()); // 头像文件名，例如 "alice.png"
                // 最近聊天记录
                friendsInfo.add(info);
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("friends", friendsInfo);
        return ResponseEntity.ok(response);
    }



}
