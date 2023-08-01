/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaolin.mpms.mapper.UserMapper;
import com.xiaolin.mpms.entity.user.AuthUser;
import com.xiaolin.mpms.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        // 判断用户是否存在
        if (Objects.isNull(user))
            // 用户不存在
            throw new UsernameNotFoundException("用户不存在");
//        if (!user.getIsEnable())
//            throw new RuntimeException("用户已被禁用");
        // 查询用户角色
//        List<Role> roles = userMapper.getRolesByUid(user.getId());
        // 查询用户权限
        List<String> list = userMapper.getUserPermissions(user.getId());
        // 返回UserDetails实现类
        return new AuthUser(user, list);
    }
}
