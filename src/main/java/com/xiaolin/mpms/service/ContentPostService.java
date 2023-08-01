/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.mpms.entity.VO.ContentPostVO;
import com.xiaolin.mpms.entity.VO.ResultVO;
import com.xiaolin.mpms.entity.content.ContentPost;

import java.util.Map;

/**
 * <p>
 * 内容发布 服务类
 * </p>
 *
 * @author xiaolin
 * @since 2023-03-22
 */
public interface ContentPostService extends IService<ContentPost> {

    Page<ContentPost> getListPage(Integer current, Integer size, Map<String, String> filter);

    String postContent(ContentPostVO contentPost);
}
