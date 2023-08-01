/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.mpms.entity.user.User;
import com.xiaolin.mpms.entity.VO.UserVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户服务层
 * </p>
 *
 * @author xiaolin
 * @since 2023-02-27
 */
public interface UserService extends IService<User> {
    /**
     * 获取用户列表
     *
     * @param current 当前页
     * @param size    每页条数
     * @param filter 过滤条件
     * @return 用户列表
     */
    Page<User> getUserListPage(int current, int size, Map<String, String> filter);

    /**
     * 获取带角色用户信息
     *
     * @return 用户信息
     */
    UserVO getUserInfo(Integer id);

    /**
     * 获取带权用户信息
     * @return 带权用户信息
     */

    UserVO getUserInfoWithPermission();

    User getUserInfo(Integer id, List<String> column);

    User getUserInfoByUid(String uid);

    User getUserInfoByUid(String uid,List<String> column);


    /**
     * 保存用户信息
     * @param user 用户信息
     * @return 是否保存成功
     */
    Boolean saveUser(UserVO user);

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 用户是否删除成功
     */
    Boolean deleteUser(int id);

    /**
     * 批量删除用户
     *
     * @param ids 用户ID列表
     * @return 用户是否删除成功
     */
    Boolean deleteUser(List<Integer> ids);

    /**
     * 判断用户是否存在
     *
     * @param username 用户名
     * @return 用户是否存在
     */
    Boolean isUserExist(String username);

    String login(String username, String password, String code, String codeKey);

    Boolean logout();

    Boolean resetPassword(int id, String initialPassword);

    Boolean updateProfile(UserVO user);

    Boolean updateUserPassword(Map<String, String> data);

    Boolean updateAvatar(UserVO user);

    List<User> getUserListSelect();
}
