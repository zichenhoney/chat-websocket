package com.chat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author LZ
 * @since 2025-03-26
 */
@Getter
@Setter
@TableName("users")
@ApiModel(value = "Users对象", description = "")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "username", type = IdType.AUTO)
    private String username;

    @TableField("password")
    private String password;

    @TableField("sex")
    private String sex;

    @TableField("avatar")
    private String avatar;

    @TableField("signature")
    private String signature;



}
