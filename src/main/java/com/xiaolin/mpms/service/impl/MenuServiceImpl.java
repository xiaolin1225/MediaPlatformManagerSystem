/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.mpms.mapper.MenuMapper;
import com.xiaolin.mpms.service.MenuService;
import com.xiaolin.mpms.entity.system.Menu;
import com.xiaolin.mpms.utils.MenuTreeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
     * @param pid    父级ID
     * @return 菜单树形数组
     */
    @Override
    public List<Menu> getRouterList(Integer pid) {
        return getMenuList(pid,null);
//        return menus;
    }

    @Override
    public List<Menu> getRoleEditList(){
        return getMenuList(null, Arrays.asList("id","pid","title","`order`"));
    }

    private List<Menu> getMenuList(Integer pid, List<String> column){
        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        if (pid != null)
            menuQueryWrapper.eq("id", pid);
        else
            pid = 0;
        if (column != null&&column.size()>0)
            menuQueryWrapper.select(column);

        List<Menu> menus = menuMapper.selectList(menuQueryWrapper);
        menus.sort((a, b) -> {
            int pOrder = b.getPid() - a.getPid();
            if (pOrder == 0) {
                return b.getOrder() - a.getOrder();
            }
            return pOrder;
        });
        return MenuTreeList.getTreeList(menus, pid);
    }

    private List<Menu> generateTreeMenuList(List<Menu> list, Integer pid) {
        return list.stream().filter(item -> item != null && Objects.equals(item.getPid(), pid))
                .peek(item -> item.setChildren(generateTreeMenuList(list, item.getId()))).collect(Collectors.toList());
    }
}
