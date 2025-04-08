package com.chat.dao;

import com.chat.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LZ
 * @since 2025-03-26
 */

@Mapper
@Repository
public interface MessageMapper extends BaseMapper<Message> {
    List<Message> getChatHistory(@Param("user") String user, @Param("friend") String friend);
}
