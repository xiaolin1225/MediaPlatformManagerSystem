/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.filter;

import com.xiaolin.mpms.entity.AuthUser;
import com.xiaolin.mpms.utils.JWTUtils;
import com.xiaolin.mpms.utils.RedisCache;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
@Log4j2
public class JWTAuthenticationTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("url:{},uri:{},header:{}", request.getRequestURI(), request.getRequestURI(),request.getHeader("Sec-WebSocket-Protocol"));
        // 获取token
        String token = request.getHeader(tokenHeader);
        if (StringUtils.isEmpty(token)) {
            String websocketToken = request.getHeader("Sec-WebSocket-Protocol");
            if (StringUtils.isEmpty(websocketToken)) {
                // 放行
                filterChain.doFilter(request, response);
                return;
            } else {
                token = websocketToken;
                response.setHeader("Sec-WebSocket-Protocol",websocketToken);
            }
        }

        // 解析token
        String uid;
        try {
            uid = jwtUtils.getUidFromToken(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 从Redis中获取用户信息
        AuthUser authUser = redisCache.getCacheObject("login:" + uid);
        System.out.println(authUser);
        if (Objects.isNull(authUser)) {
            throw new RuntimeException("用户未登录");
        }
        // 存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 放行
        filterChain.doFilter(request, response);
    }
}
