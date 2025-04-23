package com.chat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultVO {
    // 业务响应
    private Integer code;
    // 业务消息
    private String msg;
    private Boolean success;
    private Object data;
    private Integer count;

    // 新增简便构造函数
    public ResultVO(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.success = code == 200;  // 可根据业务调整
        this.data = data;
        this.count = 0;
    }
}
