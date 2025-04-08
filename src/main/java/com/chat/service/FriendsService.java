package com.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chat.entity.Friends;
import java.util.List;

public interface FriendsService extends IService<Friends> {
    boolean existsFriendship(String userName, String friendName);
    void addFriend(String userName, String friendName);

    // 添加获取好友列表的方法
    List<String> getFriendsByUsername(String userName);

}
