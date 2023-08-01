/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service.impl;

import com.xiaolin.mpms.entity.Platform.WX.WXData;
import com.xiaolin.mpms.service.WxService;
import com.xiaolin.mpms.utils.RedisCache;
import com.xiaolin.mpms.utils.platform.WXTokenData;
import com.xiaolin.mpms.utils.platform.WXUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class WxServiceImpl implements WxService {

    private String testAppid = "wxbb2d196c4f9e5670";

    private String testAppsecret = "85dfc5480715454d7a0eae19f6e9cba7";

    @Autowired
    private RedisCache redisCache;

    @Override
    public void testApi(String type) {
        String token = redisCache.getCacheObject("wxTest:accessToken");
        if (token == null) {
            WXTokenData accessToken = WXUtils.getAccessToken(testAppid, testAppsecret);
            token = accessToken.getAccess_token();
            redisCache.setCacheObject("wxTest:accessToken", token, accessToken.getExpires_in(), TimeUnit.SECONDS);
        }
        log.info("accessToken=>{}", token);
        if (type.equals("Api Domain IP")) {
            WXData wxData = WXUtils.getWXData(token, "Api Domain IP", null);
            log.info("Api Domain IP>ip_list=>{}", wxData.getIp_list());
        } else if (type.equals("Callback IP")) {
            WXData wxData = WXUtils.getWXData(token, "Callback IP", null);
            log.info("Callback IP>ip_list=>{}", wxData.getIp_list());
        }

    }
}
