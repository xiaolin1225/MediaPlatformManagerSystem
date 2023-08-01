/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.mpms.entity.VO.UserVO;
import com.xiaolin.mpms.entity.user.AuthUser;
import com.xiaolin.mpms.entity.user.Role;
import com.xiaolin.mpms.entity.user.User;
import com.xiaolin.mpms.exception.FileException;
import com.xiaolin.mpms.exception.IllegalRequestException;
import com.xiaolin.mpms.exception.UserError;
import com.xiaolin.mpms.mapper.*;
import com.xiaolin.mpms.service.UserService;
import com.xiaolin.mpms.utils.*;
import com.xiaolin.mpms.utils.text.EncryptUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private PositionMapper positionMapper;

    /**
     * 获取用户列表
     *
     * @param current 当前页
     * @param size    每页条数
     * @param filter  过滤条件
     * @return 用户列表
     */
    public Page<User> getUserListPage(int current, int size, Map<String, String> filter) {
        String isEnable = filter.getOrDefault("isEnable", "");
        String keyword = filter.getOrDefault("keyword", "");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.like("username", keyword).or().like("nickname", keyword));
        if (!isEnable.equals("")) {
            queryWrapper.eq("is_enable", isEnable);
        }
        Page<User> page = new Page<>(current, size);
        return page(page, queryWrapper);
    }

    /**
     * 获取带权用户信息
     *
     * @return 用户信息
     */
    public UserVO getUserInfoWithPermission() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        UserVO user = new UserVO(authUser.getUser());
        List<Role> roles = userMapper.getRolesByUid(user.getId());
        List<String> permissions = userMapper.getUserPermissions(user.getId());
        user.setDepartment(departmentMapper.selectById(user.getDid()));
        user.setDid(null);
        user.setPosition(positionMapper.selectById(user.getPid()));
        user.setPid(null);
        user.setRoles(roles);
        user.setPermissions(permissions);
        return user;
    }


    /**
     * 获取带角色用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @Override
    public UserVO getUserInfo(Integer id) {
        UserVO user = new UserVO(getById(id));
        List<Role> roles = userMapper.getRolesByUid(id);
        List<Integer> roleIds = new ArrayList<>();
        for (Role role : roles) {
            roleIds.add(role.getId());
        }
        user.setRoleIds(roleIds);
//        user.setRoles(roles);
        return user;
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
            AuthUser authUser = (AuthUser) authentication.getPrincipal();
            User filterUser = new User();
            User user = authUser.getUser();
            if (column != null && column.size() > 0) {
                try {
                    Field[] fields = User.class.getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        if (column.contains(field.getName())) {
                            field.set(filterUser, field.get(user));
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            return user;
        }
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
     * 保存用户
     *
     * @param user 用户信息
     * @return 用户信息是否保存成功
     */
    @Override
    @Transactional
    public Boolean saveUser(UserVO user) {
        User hasuser = getOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        if (hasuser != null && !Objects.equals(hasuser.getId(), user.getId())) {
            throw new UserError(4004, "用户名已存在");
        }
        if (user.getId() == null) {
            String encryptPassword = passwordEncoder.encode(user.getInitialPassword());
            user.setPassword(encryptPassword);
            user.setUid(UUIDUtil.getUUID());
        }
        user.setAvatar(imageToUrl(user.getAvatar()));
        saveOrUpdate(user);
        userRoleMapper.deleteRoleIdByUid(user.getId());
        List<Integer> roleIds = user.getRoleIds();
        if (!roleIds.isEmpty()) {
            return userRoleMapper.insertRoleId(user.getId(), roleIds) > 0;
        }
        return true;
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 用户是否删除成功
     */
    @Override
    public Boolean deleteUser(int id) {
        userRoleMapper.deleteRoleIdByUid(id);
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
        if (StringUtils.isEmpty(codeKey) || StringUtils.isEmpty(code)) {
            throw new UserError("验证码不能为空");
        }
        String cacheCode = redisCache.getCacheObject("captcha:login:" + codeKey);
        if (StringUtils.isEmpty(cacheCode)) {
            throw new UserError("验证码已过期");
        }
        if (!cacheCode.equalsIgnoreCase(code)) {
            throw new UserError("验证码错误");
        }
        // 用户名密码校验
        UsernamePasswordAuthenticationToken authenticationToken = null;
        authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        AuthUser authUser = (AuthUser) authenticate.getPrincipal();
        User user = authUser.getUser();
        user.setPassword(null);
        // 生成token
        String token = jwtUtils.generateToken(user, null);
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

    @Override
    public Boolean resetPassword(int id, String initialPassword) {
        String decryptInitialPassword;
        try {
            decryptInitialPassword = EncryptUtils.decryptByPrivateKey(initialPassword);
        } catch (Exception e) {
            throw new IllegalRequestException("参数不正确，请检查");
        }
        String encryptPassword = passwordEncoder.encode(decryptInitialPassword);
        User user = new User();
        user.setId(id);
        user.setInitialPassword(decryptInitialPassword);
        user.setPassword(encryptPassword);
        return updateById(user);
    }

    @Override
    public Boolean updateProfile(UserVO user) {
        // 判断是否修改敏感字段
        User oldUser = getById(user.getId());
        if (!oldUser.getUsername().equals(user.getUsername())) {
            throw new RuntimeException("用户名不允许修改");
        }
        // 判断是否修改了部门
        if (user.getDid() != null && !oldUser.getDid().equals(user.getDid())) {
            throw new UserError("非法请求");
        }
        // 判断是否修改了职位
        if (user.getPid() != null && !oldUser.getPid().equals(user.getPid())) {
            throw new UserError("非法请求");
        }
        // 判断是否修改了密码
        if (user.getPassword() != null && !oldUser.getPassword().equals(user.getPassword())) {
            throw new UserError("非法请求");
        }
        // 判断是否修改了初始密码
        if (user.getInitialPassword() != null && !oldUser.getInitialPassword().equals(user.getInitialPassword())) {
            throw new UserError("非法请求");
        }
        return updateById(user);
    }

    @Override
    public Boolean updateUserPassword(Map<String, String> data) {
        String id = data.get("id");
        String oldPassword = data.get("oldPassword");
        String newPassword = data.get("newPassword");
        String confirmPassword = data.get("confirmPassword");
        // 判断是否有参数未传输

        // 判断两次密码是否一致
        if (!newPassword.equals(confirmPassword)) {
            throw new UserError("确认密码不一致");
        }
        // 判断旧密码是否正确
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        User user = getById(authUser.getUser().getId());
        if (!user.getId().equals(Integer.parseInt(id))) {
            throw new IllegalRequestException();
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new UserError("原密码不正确");
        }
        // 判断新密码是否与旧密码一致
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new UserError("新密码不能和原密码相同");
        }
        // 修改密码
        User temp = new User();
        String encryptPassword = passwordEncoder.encode(newPassword);
        temp.setId(user.getId());
        temp.setPassword(encryptPassword);
        return updateById(temp);
    }

    @Override
    public Boolean updateAvatar(UserVO user) {
        String avatarUrl = imageToUrl(user.getAvatar());
        user.setAvatar(avatarUrl);
        return updateById(user);
    }

    @Override
    public List<User> getUserListSelect() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        List<SFunction<User, ?>> list = new ArrayList<>();
        list.add(User::getUsername);
        list.add(User::getNickname);
        list.add(User::getId);
        list.add(User::getUid);
        queryWrapper.select(list);
        return list(queryWrapper);
    }

    private String imageToUrl(String avatar) {
        Pattern pattern = Pattern.compile("^data:image/(.+?);base64,");
        Matcher matcher = pattern.matcher(avatar);
        String thumbUrl = "";
        if (matcher.find()) {
            String uploadPath = FileUtil.getUploadPath();
            String fileName = UUIDUtil.getUUID();
            String ext = FileUtil.getFileExtensionFromBase64(avatar);
            uploadPath += File.separator + "avatar" + File.separator + fileName + "." + ext;
            File avatarFile = FileUtil.base64ToFile(avatar, uploadPath);
            thumbUrl = "/upload/avatar/" + fileName + "." + ext;
        } else {
            try {
                thumbUrl = ServletUtils.getHostPath() + "/" + avatar;
                new URL(thumbUrl);
                thumbUrl = avatar;
            } catch (MalformedURLException e) {
                throw new FileException(3002, "文件格式不支持");
            }
        }
        return thumbUrl;
    }
}
