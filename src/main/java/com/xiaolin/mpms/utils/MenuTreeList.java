/*
 * Copyright (c) 2022 小林 All Rights Reserved.
 */
package com.xiaolin.mpms.utils;

import com.xiaolin.mpms.entity.system.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 小林
 * @since 2022-10-22 20:39
 **/

public class MenuTreeList {
    public static List<Menu> getTreeList(List<Menu> oList, Integer rootId) {
        List<Menu> list = new ArrayList<>();
        for (Menu menu : oList) {
            if (menu.getPid().equals(rootId)) {
                list.add(menu);
            }
        }
        oList.removeAll(list);
        for (Menu menu : list) {
            menu.setChildren(getChildrenList(oList, menu));
        }
        return list;
    }

    private static List<Menu> getChildrenList(List<Menu> oList, Menu parent) {
        List<Menu> list = new ArrayList<>();
        for (Menu menu : oList) {
            if (Objects.equals(menu.getPid(), parent.getId())) {
                list.add(menu);
            }
        }
        oList.removeAll(list);
        if (!list.isEmpty()) {
            for (Menu menu : list) {
                menu.setChildren(getChildrenList(oList, menu));
            }
        }
        return list;
    }

    public static List<Menu> createTreeList(List<Menu> oList, Integer pid) {
        return oList.stream()
                .filter(item -> item != null && item.getPid().equals(pid))
                .peek(item -> item.setChildren(createTreeList(oList, item.getId())))
                .collect(Collectors.toList());
    }
}
