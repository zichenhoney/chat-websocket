package com.chat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("friends")
public class Friends {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userName;    // 对应 users 表中的 username
    private String friendName;  // 对应 users 表中的 username

    public Friends() {}

    public Friends(String userName, String friendName) {
        this.userName = userName;
        this.friendName = friendName;
    }
}
