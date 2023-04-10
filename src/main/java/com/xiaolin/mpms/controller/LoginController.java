/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.code.kaptcha.Producer;
import com.xiaolin.mpms.service.UserService;
import com.xiaolin.mpms.entity.LoginDto;
import com.xiaolin.mpms.entity.ResultVO;
import com.xiaolin.mpms.entity.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * 登录
 */
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     *
     * @param user    登录用户数据
     * @return 登录用户信息
     */
    @PostMapping("login")
    public ResultVO<String> login(@RequestBody LoginDto user) {
        try {
            String token = userService.login(user.getUsername(),user.getPassword(),user.getCode(),user.getCodeKey());
            return ResultVO.success("登录成功", token);
        } catch (Exception e) {
            return ResultVO.error(4000, e.getMessage());
        }
    }

    @RequestMapping("logout")
    public ResultVO<String> logout() {
        try {
            userService.logout();
            return ResultVO.success("注销成功");
        } catch (Exception e) {
            return ResultVO.error(4000, e.getMessage());
        }
    }
}
