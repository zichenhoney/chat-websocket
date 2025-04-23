package com.chat.service.impl;

import java.util.List;
import com.chat.entity.Friends;
import com.chat.dao.FriendsMapper;
import com.chat.service.FriendsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FriendsServiceImpl extends ServiceImpl<FriendsMapper, Friends> implements FriendsService {

    @Autowired
    private FriendsMapper friendsMapper;

    @Override
    public boolean existsFriendship(String userName, String friendName) {
        QueryWrapper<Friends> query = new QueryWrapper<>();
        query.eq("user_name", userName).eq("friend_name", friendName);
        return count(query) > 0;  // 使用 count() 避免 getOne() 的潜在异常
    }

    @Transactional  // 添加事务，确保两条好友数据要么一起插入，要么一起失败
    @Override
    public void addFriend(String userName, String friendName) {
        if (!existsFriendship(userName, friendName)) {
            save(new Friends(userName, friendName));
            save(new Friends(friendName, userName)); // 保证双向关系
        }
    }

    // 获取好友列表
    @Override
    public List<String> getFriendsByUsername(String userName) {
        return friendsMapper.getFriendsByUsername(userName);
    }

}
