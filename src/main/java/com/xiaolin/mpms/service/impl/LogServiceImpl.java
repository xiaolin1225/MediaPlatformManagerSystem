package com.xiaolin.mpms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.mpms.entity.system.SystemLog;
import com.xiaolin.mpms.entity.user.User;
import com.xiaolin.mpms.enums.LogType;
import com.xiaolin.mpms.mapper.LogMapper;
import com.xiaolin.mpms.service.LogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.mpms.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 日志 服务实现类
 * </p>
 *
 * @author xiaolin
 * @since 2023-04-14
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, SystemLog> implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public Boolean insertOperationLog(SystemLog log) {
        return save(log);
    }

    @Override
    public Page<SystemLog> getUserLoginLog(int current, int size, Map<String, String> filter) {
        return getSystemLogPage(filter, LogType.LOGIN, current, size);
    }

    @Override
    public Page<SystemLog> getSystemLog(int current, int size, Map<String, String> filter) {
        return getSystemLogPage(filter, LogType.SYSTEM, current, size);
    }

    private Page<SystemLog> getSystemLogPage(Map<String, String> filter, LogType logType, int current, int size) {
        String status = filter.getOrDefault("status", "");
        String keyword = filter.getOrDefault("keyword", "");
        User user = SecurityUtils.getLoginUser();
        LambdaQueryWrapper<SystemLog> queryWrapper = new LambdaQueryWrapper<>();
        if (!keyword.equals("")) {
            queryWrapper.like(SystemLog::getTitle, keyword);
        }
        if (!status.equals("")) {
            queryWrapper.eq(SystemLog::getStatus, status);
        }
        queryWrapper.eq(SystemLog::getLogType, logType.ordinal());
        queryWrapper.eq(SystemLog::getOperationUser, user.getUsername());
        queryWrapper.orderByDesc(SystemLog::getCreateTime);
        Page<SystemLog> systemLogPage = new Page<>(current, size);
        return page(systemLogPage, queryWrapper);
    }

}
