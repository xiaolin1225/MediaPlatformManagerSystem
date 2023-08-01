/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.mpms.entity.user.Department;
import com.xiaolin.mpms.entity.VO.ResultVO;
import com.xiaolin.mpms.service.DepartmentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("department")
@Api(tags = "部门")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("list")
    public ResultVO<List<Department>> getDepartmentList() {
        return ResultVO.success("数据获取成功", departmentService.list());
    }

    @GetMapping("list/page")
    public ResultVO<Page<Department>> getDepartmentListPage(@RequestParam(defaultValue = "1") int current, @RequestParam(defaultValue = "5") int size, @RequestParam(required = false) Map<String, String> filter) {
        return ResultVO.success("数据获取成功", departmentService.getListPage(current, size, filter));
    }

    @GetMapping("list/tree")
    public ResultVO<List<Department>> getDepartmentTreeList(@RequestParam(required = false) Map<String, String> filter) {
        return ResultVO.success("数据获取成功", departmentService.getTreeList(filter));
    }

    @GetMapping("{id}")
    public ResultVO<Department> getDepartmentInfo(@PathVariable Integer id) {
        return ResultVO.success("数据获取成功", departmentService.getById(id));
    }

    @PostMapping()
    public ResultVO<String> saveDepartmentInfo(@RequestBody Department department) {
        departmentService.saveDepartmentInfo(department);
        return ResultVO.success("保存成功");
    }

    @PutMapping()
    public ResultVO<String> updateDepartmentInfo(@RequestBody Department department) {
        departmentService.saveDepartmentInfo(department);
        return ResultVO.success("保存成功");
    }

    @DeleteMapping("{id}")
    public ResultVO<String> deleteDepartment(@PathVariable Integer id) {
        departmentService.deleteDepartment(id);
        return ResultVO.success("删除成功");
    }

    @DeleteMapping("")
    public ResultVO<String> deleteDepartments(@RequestBody List<Integer> ids) {
        departmentService.deleteDepartments(ids);
        return ResultVO.success("删除成功");
    }
}
