package com.xiaolin.mpms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.mpms.entity.user.Department;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门 服务类
 * </p>
 *
 * @author xiaolin
 * @since 2023-04-11
 */
public interface DepartmentService extends IService<Department> {

    Boolean deleteDepartment(Integer id);

    Boolean deleteDepartments(List<Integer> ids);

    Page<Department> getListPage(int current, int size, Map<String, String> filter);

    List<Department> getTreeList(Map<String, String> filter);

    Boolean saveDepartmentInfo(Department department);
}
