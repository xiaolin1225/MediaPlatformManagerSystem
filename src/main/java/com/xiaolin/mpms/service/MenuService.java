/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.mpms.entity.Menu;

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
     * @param id 父级ID
     * @return 菜单树形数组
     */
    List<Menu> getTreeList(Integer id, Integer pid);
}
