/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.utils.text;

import com.alibaba.fastjson2.JSON;
import com.xiaolin.mpms.entity.VO.ResultVO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResUtils {

    public static <T> void Response(HttpServletResponse response, Integer code, String message, T data) throws IOException {
        response(response, code, message, data);
    }

    public static <T> void Response(HttpServletResponse response, Integer code, String message) throws IOException {
        ResUtils.response(response, code, message, null);
    }

    private static <T> void response(HttpServletResponse response, Integer code, String message, T data) throws IOException {
        response.setStatus(200);
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JSON.toJSONString(ResultVO.error(code, message, data)));
    }
}
