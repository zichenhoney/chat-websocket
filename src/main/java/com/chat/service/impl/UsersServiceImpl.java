package com.chat.service.impl;

import com.chat.entity.Users;
import com.chat.dao.UsersMapper;
import com.chat.service.UsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Random;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LZ
 * @since 2025-03-26
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {
    @Autowired
    private UsersMapper usersMapper;

    @Override
    public boolean register(Users users) {
        //根据前端传送的username 查询数据库有无对应数据
        Users user = usersMapper.selectById(users.getUsername());
        System.out.println(user);
        if (user!=null) {
            return false; // 用户已存在
        }

        // 默认签名
        if (users.getSignature() == null || users.getSignature().trim().isEmpty()) {
            users.setSignature("这个人很神秘，什么也没有留下");
        }
        //insert  插入数据 用的是前端传输的users  传输过来的有username和password 所以不需要重复写入
        // 只需要写入avatar和signature
        int insert = usersMapper.insert(users);
        if (insert>0){
            return true;
        }
        return  false;
    }

}
