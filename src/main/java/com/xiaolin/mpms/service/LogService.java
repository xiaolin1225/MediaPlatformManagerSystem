package com.xiaolin.mpms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.mpms.entity.system.SystemLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 日志 服务类
 * </p>
 *
 * @author xiaolin
 * @since 2023-04-14
 */
public interface LogService extends IService<SystemLog> {

    Boolean insertOperationLog(SystemLog log);

    Page<SystemLog> getUserLoginLog(int current, int size, Map<String, String> filter);

    Page<SystemLog> getSystemLog(int current, int size, Map<String, String> filter);
}
