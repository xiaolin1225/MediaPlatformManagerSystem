/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.xiaolin.mpms.entity.Captcha;
import com.xiaolin.mpms.entity.ResultVO;
import com.xiaolin.mpms.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("captcha")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @GetMapping()
    public ResultVO<Captcha> captcha(@RequestParam(required = false) String codeKey, String type) {
        System.out.println(codeKey);
        return ResultVO.success("数据获取成功", captchaService.generateCaptcha(codeKey, type));
    }

    /**
     * 判断验证码是否正确
     *
     * @param type    验证码类型
     * @param codeKey 验证码key
     * @param code    验证码
     * @return 验证码是否正确
     */
    @GetMapping("validation")
    public ResultVO<Boolean> isCaptchaCorrect(String type, String codeKey, String code) {
        return ResultVO.success("数据获取成功", captchaService.isCaptchaCorrect(type, codeKey, code));
    }
}
