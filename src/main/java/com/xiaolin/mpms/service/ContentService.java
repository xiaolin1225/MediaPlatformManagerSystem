/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.mpms.entity.VO.ResultVO;
import com.xiaolin.mpms.entity.content.Content;
import com.xiaolin.mpms.entity.content.ContentList;

import java.util.List;
import java.util.Map;

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

    List<Content> getContentListData(String contentId);

    Content getContentInfo(String id);

    Page<ContentList> getContentList(int current, int size, Map<String, String> filter);

    boolean removeContentList(Integer id);
}
