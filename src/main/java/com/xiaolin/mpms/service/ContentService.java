/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.mpms.entity.Content;
import com.xiaolin.mpms.entity.ContentList;

import java.util.List;

/**
 * <p>
 * 内容 服务类
 * </p>
 *
 * @author xiaolin
 * @since 2023-03-22
 */
public interface ContentService extends IService<Content> {

    String saveContentList(ContentList contentList);

    List<Content> getContent(String contentId);

    Page<ContentList> getContentList(int current, int size);
}
