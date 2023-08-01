package com.xiaolin.mpms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.mpms.entity.Platform.Platform;
import com.xiaolin.mpms.entity.VO.ResultVO;
import com.xiaolin.mpms.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 平台信息 前端控制器
 * </p>
 *
 * @author xiaolin
 * @since 2023-05-11
 */
@RestController
@RequestMapping("/platform")
public class PlatformController {

    @Autowired
    private PlatformService platformService;

    @GetMapping("list/page")
    public ResultVO<Page<Platform>> getListPage(@RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size, @RequestParam(required = false) Map<String, String> filter) {
        return ResultVO.success("数据获取成功", platformService.getListPage(current, size, filter));
    }

    @GetMapping("list/select")
    public ResultVO<List<Platform>> getSelectList() {
        return ResultVO.success("数据获取成功", platformService.getSelectList());
    }

    @GetMapping("{id}")
    public ResultVO<Platform> getInfo(@PathVariable Integer id) {
        return ResultVO.success("数据获取成功", platformService.getPlatformInfo(id));
    }

    @PostMapping()
    public ResultVO<Object> save(@RequestBody Platform platform) {
        return ResultVO.success("保存成功", platformService.saveInfo(platform));
    }

    @PutMapping()
    public ResultVO<Object> updateInfo(@RequestBody Platform platform) {
        return ResultVO.success("数据获取成功", platformService.updatePlatformInfo(platform));
    }

    @DeleteMapping("{id}")
    public ResultVO<Object> deleteInfo(@PathVariable Integer id) {
        return ResultVO.success("数据获取成功", platformService.deletePlatformInfo(id));
    }

}
