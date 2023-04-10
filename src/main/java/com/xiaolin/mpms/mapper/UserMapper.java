/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaolin.mpms.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    List<User> getUserList(@Param("start") int start, @Param("size") int size, @Param("status") String status, @Param("keyword") String keyword);

    Integer getUserTotalNum();

    Integer getUserTotalNumWithFilter(@Param("status") String status, @Param("keyword") String keyword);

    /**
     * 根据用户ID获取用户角色
     *
     * @param uid 用户ID
     * @return List<String> 角色列表
     */
    List<String> getUserRoles(@Param("uid") Integer uid);

    User getUserInfoByUid(@Param("uid") String uid);
}
