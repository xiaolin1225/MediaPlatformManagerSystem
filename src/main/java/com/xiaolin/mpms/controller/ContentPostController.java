/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.mpms.entity.VO.ContentPostVO;
import com.xiaolin.mpms.entity.VO.ResultVO;
import com.xiaolin.mpms.entity.content.ContentPost;
import com.xiaolin.mpms.service.ContentPostService;
import com.xiaolin.mpms.utils.platform.WXBaseMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("content/post")
public class ContentPostController {

    @Autowired
    private ContentPostService contentPostService;

    @GetMapping("list/page")
    public ResultVO<Page<ContentPost>> getListPage(@RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size, @RequestParam(required = false) Map<String, String> filter) {
        return ResultVO.success("数据获取成功", contentPostService.getListPage(current, size, filter));
    }

    @PostMapping("")
    public ResultVO<String> PostContent(@RequestBody ContentPostVO contentPost) {
        return ResultVO.success("发布成功", contentPostService.postContent(contentPost));
    }
}
