package com.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chat.entity.Users;
import org.springframework.stereotype.Service;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LZ
 * @since 2025-03-26
 */


@Service
public interface UsersService extends IService<Users> {
    // 可添加注册、登录等自定义方法
    boolean register(Users user);
}

