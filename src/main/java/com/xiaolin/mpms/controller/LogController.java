/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.mpms.entity.VO.ResultVO;
import com.xiaolin.mpms.entity.system.SystemLog;
import com.xiaolin.mpms.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("log")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping("login")
    public ResultVO<Page<SystemLog>> getUserLoginLog(@RequestParam(defaultValue = "1") int current, @RequestParam(defaultValue = "5") int size, @RequestParam(required = false) Map<String, String> filter) {
        return ResultVO.success("数据获取成功", logService.getUserLoginLog(current, size, filter));
    }

    @GetMapping("system")
    public ResultVO<Page<SystemLog>> getSystemLog(@RequestParam(defaultValue = "1") int current, @RequestParam(defaultValue = "5") int size, @RequestParam(required = false) Map<String, String> filter) {
        return ResultVO.success("数据获取成功", logService.getSystemLog(current, size, filter));
    }
}
