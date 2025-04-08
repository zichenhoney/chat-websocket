package com.chat.dao;

import java.util.List;
import com.chat.entity.Friends;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LZ
 * @since 2025-03-26
 */
@Repository
public interface FriendsMapper extends BaseMapper<Friends> {
    @Select("SELECT friend_name FROM friends WHERE user_name = #{userName}")
    List<String> getFriendsByUsername(@Param("userName") String userName);
}
