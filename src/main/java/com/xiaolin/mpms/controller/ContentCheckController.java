/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.mpms.entity.VO.ContentCheckVo;
import com.xiaolin.mpms.entity.VO.ResultVO;
import com.xiaolin.mpms.entity.content.ContentCheck;
import com.xiaolin.mpms.service.ContentCheckService;
import com.xiaolin.mpms.service.ContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/content/check")
@Api(tags = "内容审核")
@RestController
public class ContentCheckController {

    @Autowired
    private ContentCheckService contentCheckService;

    @PostMapping("submit")
    @ApiOperation(value = "提交审核")
    public ResultVO<String> submitCheck(@RequestBody ContentCheckVo data) {
        contentCheckService.submitCheck(data);
        return ResultVO.success("数据保存成功");
    }

    @GetMapping("list/page")
    public ResultVO<Page<ContentCheck>> getListPage(@RequestParam(defaultValue = "1") int current, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) Map<String, Object> filter) {
        return ResultVO.success(contentCheckService.getListPage(current, size, filter));
    }

    @GetMapping("{id}")
    public ResultVO<ContentCheck> getCheck(@PathVariable String id) {
        return ResultVO.success("数据获取成功", contentCheckService.getCheck(id));
    }

    @PostMapping("result")
    public ResultVO<Boolean> submitCheckResult(@RequestBody ContentCheckVo contentCheck) {
        return ResultVO.success("保存成功", contentCheckService.submitCheckResult(contentCheck));
    }
}
