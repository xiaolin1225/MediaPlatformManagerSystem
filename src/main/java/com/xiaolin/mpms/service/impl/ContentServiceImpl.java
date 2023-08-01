/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.mpms.entity.VO.ResultVO;
import com.xiaolin.mpms.exception.ContentException;
import com.xiaolin.mpms.exception.FileException;
import com.xiaolin.mpms.mapper.ContentListMapper;
import com.xiaolin.mpms.mapper.ContentMapper;
import com.xiaolin.mpms.service.ContentService;
import com.xiaolin.mpms.entity.content.Content;
import com.xiaolin.mpms.entity.content.ContentList;
import com.xiaolin.mpms.utils.FileUtil;
import com.xiaolin.mpms.utils.ServletUtils;
import com.xiaolin.mpms.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            ContentList listData = handleContents(contentId, contents);
            contentListMapper.insert(listData);
        } else {
            QueryWrapper<Content> contentQueryWrapper = new QueryWrapper<>();
            contentQueryWrapper.eq("content_id", contentId);
            remove(contentQueryWrapper);
            ContentList listData = handleContents(contentId, contents);
            LambdaQueryWrapper<ContentList> listLambdaQueryWrapper = new LambdaQueryWrapper<>();
            listLambdaQueryWrapper.eq(ContentList::getContentId, contentId);
            contentListMapper.update(listData, listLambdaQueryWrapper);
        }
        return contentId;
    }

    private ContentList handleContents(String contentId, List<Content> contents) {
        for (Content content : contents) {
            content.setContentId(contentId);
            content.setThumb(imageToUrl(content.getThumb()));
            content.setContent(content.getContent().replaceAll("\n", ""));
        }
        saveBatch(contents);
        return new ContentList(contentId, contents.size(), null, 1);
    }

    @Override
    public List<Content> getContentListData(String contentId) {
        QueryWrapper<Content> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("content_id", contentId);
        return list(queryWrapper);
    }

    @Override
    public Content getContentInfo(String id) {
        LambdaQueryWrapper<Content> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Content::getId, id).or().eq(Content::getSubId, id);
        Content content = getOne(queryWrapper);
        if (content == null) {
            throw new ContentException(6001, "内容数据不存在，请检查内容ID是否正确");
        }
        return content;
    }

    @Override
    public Page<ContentList> getContentList(int current, int size, Map<String, String> filter) {
        String title = filter.getOrDefault("keyword", "");
        Page<ContentList> page = new Page<>(current, size);
        QueryWrapper<ContentList> queryWrapper = new QueryWrapper<>();
        Page<ContentList> listPage = contentListMapper.selectPage(page, queryWrapper);
        List<ContentList> records = new ArrayList<>();
        for (ContentList record : listPage.getRecords()) {
            QueryWrapper<Content> contentQueryWrapper = new QueryWrapper<>();
            contentQueryWrapper.like("title", title);
            contentQueryWrapper.select("id", "content_id", "content_index", "sub_id", "title", "summary", "thumb", "update_time");
            contentQueryWrapper.eq("content_id", record.getContentId());
            List<Content> list = list(contentQueryWrapper);
            if (list.size() != 0) {
                record.setContents(list);
                records.add(record);
            }
        }
        listPage.setRecords(records);
        return listPage;
    }

    @Override
    public boolean removeContentList(Integer id) {
        return contentListMapper.deleteById(id) > 0;
    }

    private String imageToUrl(String thumb) {
        Pattern pattern = Pattern.compile("^data:image/(.+?);base64,");
        Matcher matcher = pattern.matcher(thumb);
        String thumbUrl = "";
        if (matcher.find()) {
            String uploadPath = FileUtil.getUploadPath();
            String fileName = UUIDUtil.getUUID();
            String ext = FileUtil.getFileExtensionFromBase64(thumb);
            uploadPath += File.separator + "thumb" + File.separator + fileName + "." + ext;
            File avatarFile = FileUtil.base64ToFile(thumb, uploadPath);
            thumbUrl = "/upload/thumb/" + fileName + "." + ext;
        } else {
            try {
                thumbUrl = ServletUtils.getHostPath() + "/" + thumb;
                new URL(thumbUrl);
                thumbUrl = thumb;
            } catch (MalformedURLException e) {
                throw new FileException(3002, "文件格式不支持");
            }
        }
        return thumbUrl;
    }
}
