/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.mpms.mapper.ContentListMapper;
import com.xiaolin.mpms.mapper.ContentMapper;
import com.xiaolin.mpms.service.ContentService;
import com.xiaolin.mpms.entity.Content;
import com.xiaolin.mpms.entity.ContentList;
import com.xiaolin.mpms.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 内容 服务实现类
 * </p>
 *
 * @author xiaolin
 * @since 2023-03-22
 */
@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, Content> implements ContentService {

    @Autowired
    private ContentListMapper contentListMapper;

    @Override
    public String saveContentList(ContentList contentList) {
        String contentId = contentList.getContentId();
        List<Content> contents = contentList.getContents();
        if (contentId == null || contentId.equals("null") || contentId.equals("")) {
            contentId = UUIDUtil.getUUID();
            for (Content content : contents) {
                content.setContentId(contentId);
            }
            saveBatch(contents);
            ContentList listData = new ContentList(contentId, contents.size(), null, 1);
            contentListMapper.insert(listData);
        } else {
            QueryWrapper<Content> contentQueryWrapper = new QueryWrapper<>();
            contentQueryWrapper.eq("content_id", contentId);
            List<Content> list = list(contentQueryWrapper);
            for (Content oldContent : list) {
                for (Content newContent : contents) {
                    if (oldContent.getSubId().equals(newContent.getSubId())) {
                        newContent.setId(oldContent.getId());
                        updateById(newContent);
                    }
                }
            }
        }
        return contentId;
    }

    @Override
    public List<Content> getContent(String contentId) {
        QueryWrapper<Content> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("content_id", contentId);
        return list(queryWrapper);
    }

    @Override
    public Page<ContentList> getContentList(int current, int size) {
        Page<ContentList> page = new Page<>(current, size);
        QueryWrapper<ContentList> queryWrapper = new QueryWrapper<>();
        Page<ContentList> listPage = contentListMapper.selectPage(page, queryWrapper);
        for (ContentList record : listPage.getRecords()) {
            QueryWrapper<Content> contentQueryWrapper = new QueryWrapper<>();
            contentQueryWrapper.select("id","content_id","content_index","sub_id","title","summary","thumb","update_time");
            contentQueryWrapper.eq("content_id", record.getContentId());
            record.setContents(list(contentQueryWrapper));
        }
        return listPage;
    }
}
