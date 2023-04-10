/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.mpms.entity.AuthUser;
import com.xiaolin.mpms.mapper.UserMapper;
import com.xiaolin.mpms.service.UserService;
import com.xiaolin.mpms.entity.User;
import com.xiaolin.mpms.utils.JWTUtils;
import com.xiaolin.mpms.utils.RedisCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户服务实现类
 * </p>
 *
 * @author xiaolin
 * @since 2023-02-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    /**
     * 获取用户列表
     *
     * @param current 当前页
     * @param size    每页条数
     * @param filter  过滤条件
     * @return 用户列表
     */
    public Page<User> getUserList(int current, int size, Map<String, String> filter) {
        String status = filter.getOrDefault("status", "");
        String keyword = filter.getOrDefault("keyword", "");
//        int total = userMapper.getUserTotalNumWithFilter(status, "%" + keyword + "%");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", keyword).or().like("nickname", keyword);
        queryWrapper.eq("status", status);
        long total = count(queryWrapper);
        if (total > 0) {
//            List<User> userList = userMapper.getUserList((current - 1) * size, size, status, "%" + keyword + "%");
//            int pageNum = total % size == 0 ? total / size : total / size + 1;
//            return new MyPage<>(current, pageNum, size, total, userList);
            Page<User> page = new Page<>(current, size);
            return page(page, queryWrapper);
        }
        return new Page<>(current, size);
//        return new MyPage<>(current, size);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @Override
    public User getUserInfo(Integer id) {
        if (id != null) {
            return getById(id);
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AuthUser user = (AuthUser) authentication.getPrincipal();
            return user.getUser();
        }
    }

    @Override
    public User getUserInfo(Integer id, List<String> column) {
        if (id != null) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            queryWrapper.select(column);
            return getById(id);
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AuthUser user = (AuthUser) authentication.getPrincipal();
            return user.getUser();
        }
    }

    @Override
    public User getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUser user = (AuthUser) authentication.getPrincipal();
        return user.getUser();
    }

    @Override
    public User getUserInfoByUid(String uid) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        return getOne(queryWrapper);
    }

    @Override
    public User getUserInfoByUid(String uid, List<String> column) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        queryWrapper.select(column);
        return getOne(queryWrapper);
    }

    /**
     * 添加用户
     *
     * @param user 用户信息
     * @return 用户是否添加成功
     */
    @Override
    public Boolean addUser(User user) {
        return save(user);
    }

    /**
     * 删除用户
     *
     * @param user 用户信息
     * @return 用户是否删除成功
     */
    @Override
    public Boolean updateUser(User user) {
        return updateById(user);
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 用户是否删除成功
     */
    @Override
    public Boolean deleteUser(int id) {
        return removeById(id);
    }

    /**
     * 批量删除用户
     *
     * @param ids 用户ID列表
     * @return 用户是否删除成功
     */
    @Override
    public Boolean deleteUser(List<Integer> ids) {
        if (ids.size() == 0)
            return true;
        return removeBatchByIds(ids);
    }

    /**
     * 判断用户是否存在
     *
     * @param username 用户名
     * @return 用户是否存在
     */
    @Override
    public Boolean isUserExist(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return getOne(queryWrapper) != null;
    }

    @Override
    public String login(String username, String password, String code, String codeKey) {
        // 验证码校验
        if (StringUtils.isEmpty(codeKey) || StringUtils.isEmpty(code))
            throw new RuntimeException("验证码不能为空");
        String cacheCode = redisCache.getCacheObject("captcha:login:" + codeKey);
        if (StringUtils.isEmpty(cacheCode))
            throw new RuntimeException("验证码已过期");
        if (!cacheCode.equalsIgnoreCase(code))
            throw new RuntimeException("验证码错误");
        // 用户名密码校验
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        AuthUser authUser = (AuthUser) authenticate.getPrincipal();
        User user = authUser.getUser();
        // 生成token
        String token = jwtUtils.generateToken(user, null);
        user.setPassword(null);
        redisCache.setCacheObject("login:" + user.getUid(), authUser);
        return token;
    }

    @Override
    public Boolean logout() {
        // 获取SecurityContextHolder中的UID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUser user = (AuthUser) authentication.getPrincipal();
        // 删除Redis中的用户信息
        return redisCache.deleteObject("login:" + user.getUser().getUid());
    }
}
