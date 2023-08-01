/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.mpms.entity.VO.ContentCheckVo;
import com.xiaolin.mpms.entity.content.ContentCheck;
import com.xiaolin.mpms.entity.content.ContentCheckUser;

import java.util.Map;

public interface ContentCheckService extends IService<ContentCheck> {
    Boolean submitCheck(ContentCheckVo data);

    Page<ContentCheck> getListPage(int current, int size, Map<String, Object> filter);

    ContentCheck getCheck(String id);

    Boolean submitCheckResult(ContentCheckVo contentCheck);
}
