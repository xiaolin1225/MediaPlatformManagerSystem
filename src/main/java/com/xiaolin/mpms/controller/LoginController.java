/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.xiaolin.mpms.annotation.Log;
import com.xiaolin.mpms.entity.VO.ResultVO;
import com.xiaolin.mpms.entity.VO.LoginVO;
import com.xiaolin.mpms.enums.LogType;
import com.xiaolin.mpms.enums.OperationStatus;
import com.xiaolin.mpms.exception.UserError;
import com.xiaolin.mpms.service.UserService;
import com.xiaolin.mpms.utils.log.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录
 */
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    private final LogUtils logUtils = new LogUtils();

    /**
     * 登录
     *
     * @param user 登录用户数据
     * @return 登录用户信息
     */
    @PostMapping("login")
    public ResultVO<String> login(@RequestBody LoginVO user) {
        String token;
        long startTime = System.currentTimeMillis();
        try {
            token = userService.login(user.getUsername(), user.getPassword(), user.getCode(), user.getCodeKey());
            logUtils.createLoginLog(user.getUsername(), startTime, OperationStatus.SUCCESS, "登录成功", null);
        } catch (Exception e) {
            logUtils.createLoginLog(user.getUsername(), startTime, OperationStatus.FAIL, "登录失败", e.getMessage());
            throw new UserError(e.getMessage(), e);
        }
        return ResultVO.success("登录成功", token);
    }

    @RequestMapping("logout")
    @Log(title = "注销登录", successMessage = "注销成功", errorMessage = "注销失败", logType = LogType.LOGIN, isSaveRequestData = false)
    public ResultVO<String> logout() {
        try {
            userService.logout();
            return ResultVO.success("注销成功");
        } catch (Exception e) {
            throw new UserError(e.getMessage(), e);
        }
    }
}