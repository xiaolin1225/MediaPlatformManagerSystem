/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.mpms.entity.Platform.Platform;
import com.xiaolin.mpms.entity.VO.ContentPostVO;
import com.xiaolin.mpms.entity.VO.ResultVO;
import com.xiaolin.mpms.entity.content.Content;
import com.xiaolin.mpms.entity.content.ContentPost;
import com.xiaolin.mpms.entity.content.ContentPostResult;
import com.xiaolin.mpms.exception.FileException;
import com.xiaolin.mpms.exception.PlatformException;
import com.xiaolin.mpms.mapper.ContentPostMapper;
import com.xiaolin.mpms.service.ContentPostResultService;
import com.xiaolin.mpms.service.ContentPostService;
import com.xiaolin.mpms.service.ContentService;
import com.xiaolin.mpms.service.PlatformService;
import com.xiaolin.mpms.utils.FileUtil;
import com.xiaolin.mpms.utils.RedisCache;
import com.xiaolin.mpms.utils.ServletUtils;
import com.xiaolin.mpms.utils.UUIDUtil;
import com.xiaolin.mpms.utils.platform.WXBaseMsg;
import com.xiaolin.mpms.utils.platform.WXMediaData;
import com.xiaolin.mpms.utils.platform.WXTokenData;
import com.xiaolin.mpms.utils.platform.WXUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 内容发布 服务实现类
 * </p>
 *
 * @author xiaolin
 * @since 2023-03-22
 */
@Service
public class ContentPostServiceImpl extends ServiceImpl<ContentPostMapper, ContentPost> implements ContentPostService {

    @Autowired
    private ContentService contentService;

    @Autowired
    private ContentPostResultService contentPostResultService;

    @Autowired
    private PlatformService platformService;

    @Autowired
    private RedisCache redisCache;


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

    @Override
    public Page<ContentPost> getListPage(Integer current, Integer size, Map<String, String> filter) {
        LambdaQueryWrapper<ContentPost> queryWrapper = new LambdaQueryWrapper<>();
        Page<ContentPost> page = new Page<>(current, size);
        page(page, queryWrapper);
        return page;
    }

    @Override
    public String postContent(ContentPostVO contentPost) {
        Integer postId = contentPost.getPostId();
        ContentPost cp = getById(postId);
        String contentId = cp.getContentId();
        LocalDateTime postTime = contentPost.getPostTime();
        List<Integer> platformIds = contentPost.getPlatformIds();
        Boolean isCurrent = contentPost.getIsCurrent();
        List<ContentPostResult> contentPostResults = new ArrayList<>();
        List<Content> listData = contentService.getContentListData(contentId);
        for (Integer platformId : platformIds) {
            ContentPostResult contentPostResult = new ContentPostResult();
            contentPostResult.setPlatformId(platformId);
            contentPostResult.setPostTime(LocalDateTime.now());
            contentPostResult.setPostId(postId);
            Platform platformInfo = platformService.getPlatformInfo(platformId);
            WXTokenData wxTokenData = JSON.parseObject(platformInfo.getAuthenticationData(), WXTokenData.class);
            String token = redisCache.getCacheObject(platformId + "token");
            if (token == null || token.isEmpty()) {
                WXTokenData accessToken = WXUtils.getAccessToken(wxTokenData.getAppid(), wxTokenData.getSecret());
                token = accessToken.getAccess_token();
                if (token == null) {
                    contentPostResult.setPostResult(JSON.toJSONString("平台连接失败，请重试"));
                }
                redisCache.setCacheObject(platformId + "token", accessToken.getAccess_token(), accessToken.getExpires_in(), TimeUnit.SECONDS);
            }
            if (token != null) {
                WXMediaData wxMediaData = WXUtils.addDraft(token, listData);
                if (wxMediaData.getMedia_id() == null) {
                    contentPostResult.setPostResult(JSON.toJSONString(wxMediaData));
                } else {
                    WXBaseMsg wxBaseMsg = WXUtils.freePublish(token, wxMediaData.getMedia_id());
                    contentPostResult.setPostResult(JSON.toJSONString(wxBaseMsg));
                }
            }
            contentPostResults.add(contentPostResult);
        }
        contentPostResultService.saveBatch(contentPostResults);
        ContentPost upC = new ContentPost();
        if (listData.size()>0){
            upC.setContentTitle(listData.get(0).getTitle());
        }
        upC.setId(postId);
        upC.setStatus(3);
        updateById(upC);
        return "发布成功";
    }
}
