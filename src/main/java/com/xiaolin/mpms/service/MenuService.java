/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.mpms.entity.system.Menu;

import java.util.List;

/**
 * <p>
 * 菜单服务类
 * </p>
 *
 * @author xiaolin
 * @since 2023-03-18
 */
public interface MenuService extends IService<Menu> {

    /**
     * 获取菜单树形数组
     *
     * @param pid    父级ID
     * @param column
     * @return 菜单树形数组
     */
    List<Menu> getRouterList(Integer pid);

    List<Menu> getRoleEditList();
}
