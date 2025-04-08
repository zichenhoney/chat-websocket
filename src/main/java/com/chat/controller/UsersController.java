package com.chat.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chat.entity.Users;
import com.chat.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LZ
 * @since 2025-03-26
 */
@Controller
@RequestMapping("user")
public class UsersController {

    @Autowired
    private UsersService usersService;



    @PostMapping ("doLogin")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> login(@RequestBody Users user, HttpSession session){

        System.out.println("进入");
        QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("username",user.getUsername());
        usersQueryWrapper.eq("password",user.getPassword());

        //根据username password 查询数据库有无对应数据
        Users users = usersService.getOne(usersQueryWrapper);
        Map<String, Object> response = new HashMap<>();

        if(null!=users) {
            // 存入 session，前端可以通过 API 获取用户名
            session.setAttribute("username", users.getUsername());

            System.out.println("Session 存储的用户名：" + session.getAttribute("username"));


            response.put("success", true);
            // 登录成功后跳转到 main.html 页面（请确保该页面存在于静态资源中，例如 /user/main.html）
            response.put("redirect", "/html/main.html");
            return ResponseEntity.ok(response);

        }else {
            response.put("success", false);
            response.put("message", "用户名或密码错误");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Users user) {
        Map<String, Object> response = new HashMap<>();

        // 为用户分配头像，假设根据用户名生成头像编号
        int avatarNumber = (int) (Math.random() * 20) + 1; // 随机选择一个头像编号（这里假设有 20 个头像）
        String avatarFileName = "avatar_" + avatarNumber + ".jpg";

        user.setAvatar(avatarFileName); // 设置用户头像路径（存储的是文件名）

        // 先查询是否存在相同的用户名
        boolean success = usersService.register(user);

        if (success) {
            response.put("success", true);
            response.put("message", "注册成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "用户名已存在");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * **获取当前登录用户**
     */
    @GetMapping("/currentUser")
    public ResponseEntity<Map<String, Object>> getCurrentUser(HttpSession session) {
        Object username = session.getAttribute("username"); // 确保用户登录时 session 里存了 username

        Map<String, Object> response = new HashMap<>();
        if (username != null) {
            response.put("username", username.toString());
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "用户未登录");
            return ResponseEntity.status(401).body(response);
        }
    }


}
