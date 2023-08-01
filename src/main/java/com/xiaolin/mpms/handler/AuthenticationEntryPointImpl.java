/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.handler;

import com.alibaba.fastjson2.JSON;
import com.xiaolin.mpms.entity.VO.ResultVO;
import com.xiaolin.mpms.exception.UserError;
import com.xiaolin.mpms.utils.text.ResUtils;
import com.xiaolin.mpms.utils.text.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String error = StringUtils.format("访问[{}]失败，登录状态已过期", request.getRequestURI());
        ResUtils.Response(response, 4002, error);
    }
}
