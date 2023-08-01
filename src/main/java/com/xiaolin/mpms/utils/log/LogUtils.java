/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.utils.log;

import com.xiaolin.mpms.controller.LoginController;
import com.xiaolin.mpms.entity.system.SystemLog;
import com.xiaolin.mpms.enums.LogType;
import com.xiaolin.mpms.enums.OperationStatus;
import com.xiaolin.mpms.enums.OperationType;
import com.xiaolin.mpms.mapper.LogMapper;
import com.xiaolin.mpms.utils.ServletUtils;
import com.xiaolin.mpms.utils.ip.AddressUtils;
import com.xiaolin.mpms.utils.ip.IpUtils;
import com.xiaolin.mpms.utils.spring.SpringUtils;
import org.springframework.scheduling.annotation.Async;

public class LogUtils {

    @Async
    public void createLoginLog(String username, long startTime, OperationStatus status, String message, String error) {
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;
        SystemLog log = new SystemLog();
        log.setTitle("用户登录");
        log.setOperationType(OperationType.OTHER.ordinal());
        String className = LoginController.class.getName();
        log.setMethod(className + "." + "login" + "()");
        log.setRequestMethod(ServletUtils.getRequest().getMethod());
        log.setLogType(LogType.LOGIN.ordinal());
        log.setOperationUser(username);
        log.setOperationUrl("/login");
        log.setOperationIp(IpUtils.getIpAddr());
        log.setOperationLocation(AddressUtils.getRealAddressByIP(log.getOperationIp()));
        log.setOperationMessage(message);
        log.setErrorMessage(error);
        log.setStatus(status.ordinal());
        log.setCostTime(costTime);
        SpringUtils.getBean(LogMapper.class).insert(log);
    }
}
