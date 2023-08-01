/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.mpms.entity.user.Role;
import com.xiaolin.mpms.exception.RoleException;
import com.xiaolin.mpms.mapper.RoleMapper;
import com.xiaolin.mpms.mapper.UserRoleMapper;
import com.xiaolin.mpms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户角色关联表 服务实现类
 * </p>
 *
 * @author xiaolin
 * @since 2023-03-28
 */


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<Role> getList() {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        return super.list(queryWrapper);
    }

    @Override
    public Page<Role> getListPage(int current, int size, Map<String, String> filter) {
        String status = filter.getOrDefault("status", "");
        String keyword = filter.getOrDefault("keyword", "");
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("`name`", keyword);
        if (!status.equals("")) {
            queryWrapper.eq("status", status);
        }
        Page<Role> page = new Page<>(current, size);
        return page(page, queryWrapper);
    }

    @Override
    public Role getRoleInfoById(Integer id) {
        return roleMapper.getRoleInfoById(id);
    }

    @Override
    @Transactional
    public Boolean saveRoleInfo(Role role) {
        Role sameName = getOne(new QueryWrapper<Role>().eq("`name`", role.getName()));
        if (sameName != null && !sameName.getId().equals(role.getId())) {
            throw new RoleException(4011,"角色已存在");
        }
        Role sameKey = getOne(new QueryWrapper<Role>().eq("`key`", role.getKey()));
        if (sameKey != null && !sameKey.getId().equals(role.getId())) {
            throw new RoleException(4012,"角色标识已存在");
        }
        role.setKey(role.getKey().toUpperCase());
        boolean res = saveOrUpdate(role);
        if (res) {
            List<Integer> menuIds = role.getMenuIds();
            Integer roleId = role.getId();
            userRoleMapper.deleteMenuIdByRid(roleId);
            if (menuIds != null && !menuIds.isEmpty()) {
                return userRoleMapper.insertMenuId(roleId, menuIds) > 0;
            }
            return true;
        }
        return false;
    }
}
