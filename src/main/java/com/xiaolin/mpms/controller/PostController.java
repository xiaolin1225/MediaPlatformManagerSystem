/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.mpms.entity.VO.ResultVO;
import com.xiaolin.mpms.entity.user.Position;
import com.xiaolin.mpms.service.PositionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("position")
@Api(tags = "职位")
public class PostController {

    @Autowired
    private PositionService positionService;

    @GetMapping("list")
    public ResultVO<List<Position>> getPositionList() {
        return ResultVO.success("数据获取成功", positionService.getList());
    }

    @GetMapping("list/page")
    public ResultVO<Page<Position>> getPositionListPage(@RequestParam(defaultValue = "1") int current, @RequestParam(defaultValue = "5") int size, @RequestParam(required = false) Map<String, String> filter) {
        return ResultVO.success("数据获取成功", positionService.getListPage(current, size, filter));
    }

    @GetMapping("{id}")
    public ResultVO<Position> getPositionInfo(@PathVariable Integer id) {
        return ResultVO.success("数据获取成功", positionService.getById(id));
    }

    @PostMapping()
    public ResultVO<String> savePositionInfo(@RequestBody Position position) {
        positionService.savePositionInfo(position);
        return ResultVO.success("保存成功");
    }

    @PutMapping()
    public ResultVO<String> updatePositionInfo(@RequestBody Position position) {
        positionService.savePositionInfo(position);
        return ResultVO.success("保存成功");
    }

    @DeleteMapping("{id}")
    public ResultVO<String> deletePosition(@PathVariable Integer id) {
        positionService.deletePosition(id);
        return ResultVO.success("删除成功");
    }

    @DeleteMapping("")
    public ResultVO<String> deletePositions(@RequestBody List<Integer> ids) {
        positionService.deletePositions(ids);
        return ResultVO.success("删除成功");
    }
}
