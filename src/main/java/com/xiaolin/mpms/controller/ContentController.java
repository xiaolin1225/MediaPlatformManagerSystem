/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.mpms.service.ContentService;
import com.xiaolin.mpms.entity.Content;
import com.xiaolin.mpms.entity.ContentList;
import com.xiaolin.mpms.entity.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/content")
@RestController
public class ContentController {

    @Autowired
    private ContentService contentService;

    @PostMapping("save")
    public ResultVO<String> save(@RequestBody ContentList contentList) {
        return ResultVO.success("内容保存成功", contentService.saveContentList(contentList));
    }

    @PostMapping("test")
    public ResultVO<String> test(@RequestBody ContentList contentList) {
        System.out.println(contentList);
        return ResultVO.success("内容保存成功");
    }

    @GetMapping("{id}")
    public ResultVO<List<Content>> getContent(@PathVariable String id) {
        return ResultVO.success("数据获取成功", contentService.getContent(id));
    }

    @GetMapping("list")
    public ResultVO<Page<ContentList>> getContentList(@RequestParam(defaultValue = "1") int current, @RequestParam(defaultValue = "10")int size) {
        return ResultVO.success("数据获取成功", contentService.getContentList(current, size));
    }
}
