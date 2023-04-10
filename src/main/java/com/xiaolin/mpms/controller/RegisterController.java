/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.google.code.kaptcha.Producer;
import com.xiaolin.mpms.service.UserService;
import com.xiaolin.mpms.entity.LoginDto;
import com.xiaolin.mpms.entity.ResultVO;
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
@RequestMapping("register")
public class RegisterController {

    @Autowired
    private Producer mathProducer;

    @Autowired
    private UserService userService;


    @GetMapping("code")
    public void captcha(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store,no-cache,must-revalidate");
        response.setHeader("Cache-Control", "post-check=0,pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpg");
        //生成验证码字符文本
        String capStr, code;
        BufferedImage image;
        String capText = mathProducer.createText();
        capStr = capText.substring(0, capText.lastIndexOf("@"));
        code = capText.substring(capText.lastIndexOf("@") + 1);
        image = mathProducer.createImage(capStr);
        session.setAttribute("register_code", code);
        System.out.println("注册验证码===>" + code);
        try {
            ServletOutputStream out = response.getOutputStream();//二进制的图片，用getOutputStream()方法
            ImageIO.write(image, "jpg", out);
            out.flush();//立即输出
            out.close();//关闭输出
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断验证码是否正确
     *
     * @param user 登录用户数据
     * @return 验证码是否正确
     */
    @PostMapping("code/validation")
    public ResultVO<Boolean> isCodeCorrect(@RequestBody LoginDto user, HttpSession session) {
        if (Objects.isNull(user))
            return ResultVO.success("数据获取成功", false);
        String code = (String) session.getAttribute("register_code");
        return ResultVO.success("数据获取成功", user.getCode().equalsIgnoreCase(code));
    }

}
