package com.xiaolin.mpms.utils;

import com.xiaolin.mpms.entity.user.AuthUser;
import com.xiaolin.mpms.entity.user.User;
import com.xiaolin.mpms.exception.DepartmentException;
import com.xiaolin.mpms.exception.UserError;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 安全服务工具类
 */
public class SecurityUtils {
    /**
     * 用户ID
     **/
    public static Integer getUserId() {
        try {
            return getLoginUser().getId();
        } catch (Exception e) {
            throw new UserError("获取用户ID异常");
        }
    }

    public static String getUserUid() {
        try {
            return getLoginUser().getUid();
        } catch (Exception e) {
            throw new UserError("获取用户UID异常");
        }
    }

    /**
     * 获取部门ID
     **/
    public static Integer getDeptId() {
        try {
            return getLoginUser().getDid();
        } catch (Exception e) {
            throw new DepartmentException("获取部门ID异常");
        }
    }

    /**
     * 获取用户名
     **/
    public static String getUsername() {
        try {
            return getLoginUser().getUsername();
        } catch (Exception e) {
            throw new UserError("获取用户名异常");
        }
    }

    /**
     * 获取用户
     **/
    public static User getLoginUser() {
        try {
            AuthUser authUser = (AuthUser) getAuthentication().getPrincipal();
            return authUser.getUser();
        } catch (Exception e) {
            throw new UserError("获取用户信息异常", e);
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }
}
