/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.mpms.entity.user.Role;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户角色关联表 服务类
 * </p>
 *
 * @author xiaolin
 * @since 2023-03-28
 */
public interface RoleService extends IService<Role> {

    List<Role> getList();

    Page<Role> getListPage(int current, int size, Map<String, String> filter);

    Role getRoleInfoById(Integer id);

    Boolean saveRoleInfo(Role role);
}
