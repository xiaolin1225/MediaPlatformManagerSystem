/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaolin.mpms.entity.user.Role;
import com.xiaolin.mpms.entity.user.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    List<User> getUserList(@Param("start") int start, @Param("size") int size, @Param("status") String status, @Param("keyword") String keyword);

    Integer getUserTotalNum();

    Integer getUserTotalNumWithFilter(@Param("status") String status, @Param("keyword") String keyword);

    /**
     * 获取用户角色
     * @param rid  List<Integer> 角色ID
     * @return List<Role> 角色列表
     */
    List<Role> getRolesByUid(@Param("rid") List<Integer> rid);

    /**
     * 根据用户ID获取用户权限
     *
     * @param uid 用户ID
     * @return List<String> 权限列表
     */
    List<String> getUserPermissions(@Param("uid") Integer uid);

    User getUserInfoByUid(@Param("uid") String uid);

    List<Role> getRolesByUid(@Param("uid") Integer uid);

    List<Integer> getRolesIdByUid(@Param("uid") Integer uid);

    Integer deleteBatchRoleByUid(@Param("ids") List<Integer> ids, @Param("uid") Integer uid);

    Integer insertBatchRoleByUid(@Param("ids") List<Integer> ids, @Param("uid") Integer uid);
}
