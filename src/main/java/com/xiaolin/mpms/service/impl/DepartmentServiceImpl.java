/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.mpms.entity.user.Department;
import com.xiaolin.mpms.entity.user.User;
import com.xiaolin.mpms.exception.DepartmentException;
import com.xiaolin.mpms.mapper.DepartmentMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.mpms.mapper.UserMapper;
import com.xiaolin.mpms.service.DepartmentService;
import com.xiaolin.mpms.utils.DepartmentTreeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门 服务实现类
 * </p>
 *
 * @author xiaolin
 * @since 2023-04-11
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Boolean deleteDepartment(Integer id) {
        long didCount = userMapper.selectCount(new QueryWrapper<User>().eq("did", id));
        if (didCount > 0) {
            throw new DepartmentException(4021, "该部门存在用户，禁止删除");
        }
        long cCount = count(new QueryWrapper<Department>().eq("pid", id));
        if (cCount > 0) {
            throw new DepartmentException(4022, "该部门存在子部门，禁止删除");
        }
        return removeById(id);
    }

    @Override
    public Boolean deleteDepartments(List<Integer> ids) {
        int size = ids.size();
        int count = 0;
        for (Integer id : ids) {
            if (deleteDepartment(id)) {
                count++;
            }
        }
        return size == count;
    }

    @Override
    public Page<Department> getListPage(int current, int size, Map<String, String> filter) {
        String status = filter.getOrDefault("status", "");
        String keyword = filter.getOrDefault("keyword", "");
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("`name`", keyword);
        if (!status.equals("")) {
            queryWrapper.eq("status", status);
        }
        Page<Department> page = new Page<>(current, size);
        return page(page, queryWrapper);
    }

    @Override
    public List<Department> getTreeList(Map<String, String> filter) {
        String status = filter.getOrDefault("status", "");
        String keyword = filter.getOrDefault("keyword", "");
        if (status.trim().equals("") && keyword.trim().equals("")) {
            return DepartmentTreeList.getTreeList(list(), 0);
        } else {
            QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("`name`", keyword);
            if (!status.equals("")) {
                queryWrapper.eq("status", status);
            }
            return list(queryWrapper);
        }
    }

    @Override
    public Boolean saveDepartmentInfo(Department department) {
        long repeatCount = count(new QueryWrapper<Department>().eq("`name`", department.getName()));
        if (repeatCount > 0) {
            throw new DepartmentException(4003, "部门名称已存在");
        }
        if (department.getPid() != null && department.getPid().equals(department.getId())) {
            throw new DepartmentException(4004, "上级部门不能是自己");
        }
        if (department.getStatus() != 1 && count(new QueryWrapper<Department>().eq("pid", department.getId()).eq("status", 1)) > 0) {
            throw new DepartmentException(4005, "该部门包含未停用的子部门!");
        }
        return saveOrUpdate(department);
    }
}
