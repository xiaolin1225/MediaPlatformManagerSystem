package com.xiaolin.mpms.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.mpms.entity.VO.ContentCheckVo;
import com.xiaolin.mpms.entity.VO.ContentVO;
import com.xiaolin.mpms.entity.content.Content;
import com.xiaolin.mpms.entity.content.ContentCheck;
import com.xiaolin.mpms.entity.content.ContentCheckUser;
import com.xiaolin.mpms.entity.content.ContentPost;
import com.xiaolin.mpms.exception.ContentException;
import com.xiaolin.mpms.exception.FileException;
import com.xiaolin.mpms.mapper.ContentCheckMapper;
import com.xiaolin.mpms.mapper.ContentCheckUserMapper;
import com.xiaolin.mpms.mapper.ContentMapper;
import com.xiaolin.mpms.mapper.ContentPostMapper;
import com.xiaolin.mpms.service.ContentCheckService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.mpms.utils.FileUtil;
import com.xiaolin.mpms.utils.SecurityUtils;
import com.xiaolin.mpms.utils.ServletUtils;
import com.xiaolin.mpms.utils.UUIDUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xiaolin
 * @since 2023-05-10
 */
@Service
@Log4j2
public class ContentCheckServiceImpl extends ServiceImpl<ContentCheckMapper, ContentCheck> implements ContentCheckService {

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private ContentCheckMapper contentCheckMapper;

    @Autowired
    private ContentPostMapper contentPostMapper;

    @Autowired
    private ContentCheckUserMapper contentCheckUserMapper;

    @Override
    public Boolean submitCheck(ContentCheckVo data) {
        String uid = SecurityUtils.getUserUid();
        List<ContentVO> contents = data.getContents();
        List<ContentCheckUser> checkUser = data.getCheckUser();
        LambdaQueryWrapper<Content> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Collections.singletonList(Content::getTitle));
        for (ContentVO content : contents) {
            ContentCheck contentCheck = new ContentCheck();
            contentCheck.setContentId(content.getId());
            contentCheck.setContentTitle(content.getTitle());
            contentCheck.setProcess("待审核");
            contentCheck.setUid(uid);
            contentCheck.setProcessIndex(0);
            contentCheck.setProcessTotal(checkUser.size());
            ContentCheck one = getOne(new LambdaQueryWrapper<ContentCheck>().select(Arrays.asList(ContentCheck::getId, ContentCheck::getCheckVersion)).eq(ContentCheck::getContentId, content.getId()));
            Integer version = 1;
            if (one != null) {
                contentCheck.setId(one.getId());
                version = one.getCheckVersion() + 1;
                contentCheck.setCheckVersion(version);
                contentCheck.setProcessIndex(0);
                updateById(contentCheck);
            } else {
                contentCheck.setCheckVersion(version);
                save(contentCheck);
            }
            for (ContentCheckUser contentCheckUser : checkUser) {
//                if (contentCheckUser.getUid().equals(uid)) {
//                    removeById(contentCheck.getId());
//                    throw new ContentException("不能将自己作为审核人员");
//                }
                contentCheckUser.setCheckVersion(version);
                contentCheckUser.setCheckId(contentCheck.getId());
                contentCheckUserMapper.insert(contentCheckUser);
            }
        }
        return true;
    }

    @Override
    public Page<ContentCheck> getListPage(int current, int size, Map<String, Object> filter) {
        String uid = SecurityUtils.getUserUid();
        LambdaQueryWrapper<ContentCheck> queryWrapper = new LambdaQueryWrapper<>();
        Page<ContentCheck> page = new Page<>(current, size);
        contentCheckMapper.getListPage(page, queryWrapper);
        for (ContentCheck check : page.getRecords()) {
            check.setIsCreateUser(check.getUid().equals(SecurityUtils.getUserUid()));
            check.setEnableCheck(check.getCheckUsers().stream().anyMatch(item -> item.getUid().equals(uid) && item.getIndex().equals(check.getProcessIndex())));
            check.setCheckUsers(check.getCheckUsers().stream().filter(item -> item.getCheckVersion().equals(check.getCheckVersion())).collect(Collectors.toList()));
        }
        return page;
    }

    @Override
    public ContentCheck getCheck(String id) {
        String uid = SecurityUtils.getUserUid();
        contentCheckUserMapper.updateStatusByUid(uid);
        LambdaQueryWrapper<ContentCheck> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContentCheck::getId, id);
        ContentCheck check = contentCheckMapper.getCheck(queryWrapper);
        if (check.getStatus().equals(1)) {
            ContentCheck temp = new ContentCheck();
            temp.setProcess("正在审核");
            temp.setStatus(2);
            temp.setId(Integer.valueOf(id));
            contentCheckMapper.updateById(temp);
        }
        Content content = contentMapper.selectOne(new LambdaQueryWrapper<Content>().eq(Content::getSubId, check.getContentId()));
        check.setContent(content);
        check.setIsCreateUser(check.getUid().equals(SecurityUtils.getUserUid()));
        if (check.getStatus().equals(1) || check.getStatus().equals(2)) {
            check.setEnableCheck(check.getCheckUsers().stream().anyMatch(item -> item.getUid().equals(uid) && item.getIndex().equals(check.getProcessIndex())));
        } else {
            check.setEnableCheck(false);
        }
        check.setCheckUsers(check.getCheckUsers().stream().filter(item -> item.getCheckVersion().equals(check.getCheckVersion())).collect(Collectors.toList()));
        return check;
    }

    @Override
    public Boolean submitCheckResult(ContentCheckVo contentCheck) {
        Integer index = contentCheck.getProcessIndex();
        LambdaQueryWrapper<ContentCheckUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContentCheckUser::getCheckId, contentCheck.getId());
        queryWrapper.eq(ContentCheckUser::getCheckVersion, contentCheck.getCheckVersion());
        queryWrapper.eq(ContentCheckUser::getIndex, index);
        ContentCheckUser checkUser = contentCheckUserMapper.selectOne(queryWrapper);
        checkUser.setCheckResult(imageToUrl(contentCheck.getCheckResult()));
        checkUser.setCommit(contentCheck.getCommit());
        ContentCheck check = new ContentCheck();
        check.setId(contentCheck.getId());
        Integer status = contentCheck.getStatus();
        checkUser.setStatus(status);
        check.setStatus(status);
        LambdaQueryWrapper<ContentCheck> checkQueryWrapper = new LambdaQueryWrapper<>();
        checkQueryWrapper.eq(ContentCheck::getId, contentCheck.getId());
        ContentCheck one = getOne(checkQueryWrapper);
        if (status == 3) {
            if (index + 1 == one.getProcessTotal()) {
                check.setProcess("已通过");
            } else {
                check.setProcess(checkUser.getLevel() + "通过");
            }
        } else if (status == 0) {
            check.setProcess("已退回");
        }
        if (updateById(check)) {
            saveContentPost(one.getContentId());
            return contentCheckUserMapper.updateById(checkUser) > 0;
        }
        return false;
    }

    @Async
    public void saveContentPost(String subId) {
        LambdaQueryWrapper<Content> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Content::getSubId, subId);
        Content content = contentMapper.selectOne(queryWrapper);
        String contentId = content.getContentId();
        queryWrapper.clear();
        queryWrapper.eq(Content::getContentId, contentId);
        List<Content> contents = contentMapper.selectList(queryWrapper);
        LambdaQueryWrapper<ContentCheck> contentCheckQueryWrapper = new LambdaQueryWrapper<>();
        contentCheckQueryWrapper.eq(ContentCheck::getStatus, 3);
        contentCheckQueryWrapper.in(ContentCheck::getContentId, contents.stream().map(Content::getSubId).collect(Collectors.toList()));
        long count = count(contentCheckQueryWrapper);
        log.info("count=>{}", count);
        if (count == contents.size()) {
            ContentPost contentPost = new ContentPost();
            contentPost.setContentId(contentId);
            contentPostMapper.insert(contentPost);
        }
    }

    private String imageToUrl(String thumb) {
        Pattern pattern = Pattern.compile("^data:image/(.+?);base64,");
        Matcher matcher = pattern.matcher(thumb);
        String thumbUrl = "";
        if (matcher.find()) {
            String uploadPath = FileUtil.getUploadPath();
            String fileName = UUIDUtil.getUUID();
            String ext = FileUtil.getFileExtensionFromBase64(thumb);
            uploadPath += File.separator + "check-result" + File.separator + fileName + "." + ext;
            File avatarFile = FileUtil.base64ToFile(thumb, uploadPath);
            thumbUrl = "/upload/check-result/" + fileName + "." + ext;
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
