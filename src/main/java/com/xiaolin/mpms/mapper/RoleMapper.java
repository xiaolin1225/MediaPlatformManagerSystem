/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaolin.mpms.entity.user.Role;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户角色关联表 Mapper 接口
 * </p>
 *
 * @author xiaolin
 * @since 2023-03-28
 */
public interface RoleMapper extends BaseMapper<Role> {

    Role getRoleInfoById(@Param("id") Integer id);
}
