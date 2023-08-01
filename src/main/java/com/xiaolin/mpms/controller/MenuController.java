/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.xiaolin.mpms.service.MenuService;
import com.xiaolin.mpms.entity.system.Menu;
import com.xiaolin.mpms.entity.VO.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
@Api(tags = "菜单")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("")
    @ApiOperation(value = "获取菜单数据")
    @ApiResponses({
            @ApiResponse(code = 200, message = "数据获取成功")
    })
    public ResultVO<List<Menu>> getMenuList(@RequestParam(required = false) Integer pid) {
        List<Menu> treeList = menuService.getRouterList(pid);
        return ResultVO.success("数据获取成功", treeList);
    }

    @GetMapping("/role")
    @ApiOperation(value = "获取角色编辑菜单数据")
    @ApiResponses({
            @ApiResponse(code = 200, message = "数据获取成功")
    })
    public ResultVO<List<Menu>> getRoleEditList() {
        List<Menu> treeList = menuService.getRoleEditList();
        return ResultVO.success("数据获取成功", treeList);
    }
}
