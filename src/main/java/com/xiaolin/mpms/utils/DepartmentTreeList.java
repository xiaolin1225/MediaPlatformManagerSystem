/*
 * Copyright (c) 2022 小林 All Rights Reserved.
 */
package com.xiaolin.mpms.utils;

import com.xiaolin.mpms.entity.user.Department;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 小林
 * @since 2022-10-22 20:39
 **/

public class DepartmentTreeList {
    public static List<Department> getTreeList(List<Department> oList, Integer rootId) {
        List<Department> list = new ArrayList<>();
        for (Department menu : oList) {
            if (menu.getPid().equals(rootId)) {
                list.add(menu);
            }
        }
        oList.removeAll(list);
        for (Department menu : list) {
            menu.setChildren(getChildrenList(oList, menu));
        }
        return list;
    }

    private static List<Department> getChildrenList(List<Department> oList, Department parent) {
        List<Department> list = new ArrayList<>();
        for (Department menu : oList) {
            if (Objects.equals(menu.getPid(), parent.getId())) {
                list.add(menu);
            }
        }
        oList.removeAll(list);
        if (!list.isEmpty()) {
            for (Department menu : list) {
                menu.setChildren(getChildrenList(oList, menu));
            }
        }
        return list;
    }

    public static List<Department> createTreeList(List<Department> oList, Integer pid) {
        return oList.stream()
                .filter(item -> item != null && item.getPid().equals(pid))
                .peek(item -> item.setChildren(createTreeList(oList, item.getId())))
                .collect(Collectors.toList());
    }
}
