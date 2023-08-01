/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.mpms.service.ContentService;
import com.xiaolin.mpms.entity.content.Content;
import com.xiaolin.mpms.entity.content.ContentList;
import com.xiaolin.mpms.entity.VO.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        return ResultVO.success("内容保存成功");
    }

    @GetMapping("list/{id}")
    public ResultVO<List<Content>> getContentList(@PathVariable String id) {
        return ResultVO.success("数据获取成功", contentService.getContentListData(id));
    }

    @GetMapping("list")
    public ResultVO<Page<ContentList>> getContentList(@RequestParam(defaultValue = "1") int current, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) Map<String, String> filter) {
        return ResultVO.success("数据获取成功", contentService.getContentList(current, size, filter));
    }

    @DeleteMapping("list/{id}")
    public ResultVO<Object> removeContentList(@PathVariable Integer id) {
        return ResultVO.success("删除成功", contentService.removeContentList(id));
    }

    @GetMapping("{id}")
    public ResultVO<Content> getContent(@PathVariable String id) {
        return ResultVO.success("数据获取成功", contentService.getContentInfo(id));
    }
}
