/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.mpms.mapper.MenuMapper;
import com.xiaolin.mpms.service.MenuService;
import com.xiaolin.mpms.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xiaolin
 * @since 2023-03-18
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    /**
     * 获取菜单树形数组
     *
     * @param id 父级ID
     * @return 菜单树形数组
     */
    @Override
    public List<Menu> getTreeList(Integer id, Integer pid) {
        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        if (id != null)
            menuQueryWrapper.eq("id", id);
        if (pid != null)
            menuQueryWrapper.eq("pid", pid);
        else
            pid = 0;
//        List<Menu> menus = menuMapper.selectList(menuQueryWrapper);
//        return generateTreeMenuList(menus, pid);
        return menuMapper.selectList(menuQueryWrapper);
    }

    private List<Menu> generateTreeMenuList(List<Menu> list, Integer pid) {
        return list.stream().filter(item -> item != null && Objects.equals(item.getPid(), pid))
                .peek(item -> item.setChildren(generateTreeMenuList(list, item.getId())))
                .collect(Collectors.toList());
    }
}
