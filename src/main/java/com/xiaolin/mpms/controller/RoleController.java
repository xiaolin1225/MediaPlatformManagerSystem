/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.mpms.entity.VO.ResultVO;
import com.xiaolin.mpms.entity.user.Role;
import com.xiaolin.mpms.service.RoleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("role")
@Api(tags = "角色")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("list")
    public ResultVO<List<Role>> getRoleList() {
        return ResultVO.success("数据获取成功", roleService.getList());
    }

    @GetMapping("list/page")
    public ResultVO<Page<Role>> getRoleListPage(@RequestParam(defaultValue = "1") int current, @RequestParam(defaultValue = "5") int size, @RequestParam(required = false) Map<String, String> filter) {
        return ResultVO.success("数据获取成功", roleService.getListPage(current, size, filter));
    }

    @GetMapping("{id}")
    public ResultVO<Role> getRoleInfo(@PathVariable Integer id) {
        return ResultVO.success("数据获取成功", roleService.getRoleInfoById(id));
    }

    @PostMapping()
    public ResultVO<String> saveRoleInfo(@RequestBody Role role) {
        roleService.saveRoleInfo(role);
        return ResultVO.success("保存成功");
    }

    @PutMapping()
    public ResultVO<String> updateRoleInfo(@RequestBody Role role) {
        roleService.saveRoleInfo(role);
        return ResultVO.success("保存成功");
    }

    @DeleteMapping("{id}")
    public ResultVO<String> deleteRole(@PathVariable String id) {
        roleService.removeById(id);
        return ResultVO.success("删除成功");
    }

    @DeleteMapping("")
    public ResultVO<String> deleteRoles(@RequestBody List<Integer> ids) {
        roleService.removeByIds(ids);
        return ResultVO.success("删除成功");
    }
}
