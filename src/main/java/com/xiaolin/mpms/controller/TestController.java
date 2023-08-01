/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.xiaolin.mpms.entity.content.Content;
import com.xiaolin.mpms.entity.content.ContentList;
import com.xiaolin.mpms.mapper.MediaTypeMapper;
import com.xiaolin.mpms.mapper.UserMapper;
import com.xiaolin.mpms.entity.media.MediaType;
import com.xiaolin.mpms.entity.VO.ResultVO;
import com.xiaolin.mpms.entity.user.User;
import com.xiaolin.mpms.service.ContentService;
import com.xiaolin.mpms.utils.RedisCache;
import com.xiaolin.mpms.utils.UUIDUtil;
import com.xiaolin.mpms.utils.platform.WXBaseMsg;
import com.xiaolin.mpms.utils.platform.WXMediaData;
import com.xiaolin.mpms.utils.platform.WXTokenData;
import com.xiaolin.mpms.utils.platform.WXUtils;
import com.xiaolin.mpms.utils.text.EncryptUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("utils")
public class TestController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MediaTypeMapper mediaTypeMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ContentService contentService;

    @Autowired
    private RedisCache redisCache;

    private String appid = "wx3eb89c91573749ec";
    private String secret = "6274d3fbe2b5f24accb4b89ef4f6e52b";

//    @Autowired
//    private EncryptUtils encryptUtils;

    @GetMapping("draft")
    public ResultVO<WXBaseMsg> addDraft() {
        String token = redisCache.getCacheObject("accessToken");
        if (token == null || token.isEmpty()) {
            WXTokenData accessToken = WXUtils.getAccessToken(appid, secret);
            token = accessToken.getAccess_token();
            redisCache.setCacheObject("accessToken", accessToken.getAccess_token(), accessToken.getExpires_in(), TimeUnit.SECONDS);
        }
        List<Content> listData = contentService.getContentListData("eb933dc47cb44d2db7ae76928a39a860");
        WXMediaData wxMediaData = WXUtils.addDraft(token, listData);
        return ResultVO.success("保存成功", WXUtils.freePublish(token, wxMediaData.getMedia_id()));
    }

    @PostMapping("uuid")
    public Map<String, Object> generateUUIDAndPassword(String password) {
        String uuid = UUIDUtil.getUUID();
        String salt = RandomStringUtils.randomAlphanumeric(6);
        String encryptPassword = DigestUtils.md5Hex(salt + password);
        Map<String, Object> data = new HashMap<>();
        data.put("uuid", uuid);
        data.put("salt", salt);
        data.put("encryptPassword", encryptPassword);
        return data;
    }

    @GetMapping("users")
    public List<User> generateUsers(int count) {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setUid(UUIDUtil.getUUID());
            user.setUsername("user" + i);
            user.setNickname("用户" + i);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setAvatar("http://localhost:8083/directlink/localhost/avatar.jpg");
            user.setEmail("user" + i + "@xiaolin.com");
            user.setPhone(RandomStringUtils.randomNumeric(11));
            userMapper.insert(user);
            list.add(user);
        }
        return list;
    }

    @PostMapping("file-type")
    public ResultVO<String> generateFileType(@RequestParam(defaultValue = "0") int folderId, @RequestBody(required = false) List<String> types) {
        if (types != null) {
            int count = 0;
            for (String type : types) {
                MediaType mediaType = new MediaType(folderId, type, type);
                count += mediaTypeMapper.insert(mediaType);
            }
            return ResultVO.success("数据保存成功", String.format("成功插入%d条数据，共%d条数据", count, types.size()));
        }
        return ResultVO.error(70001, "数据保存失败,数据为空");
    }

    @PostMapping("encrypt")
    public ResultVO<String> encrypt(String content) throws Exception {
        return ResultVO.success("数据获取成功", EncryptUtils.decryptByPrivateKey(content));
    }

    @GetMapping("keypair")
    public ResultVO<Object> generateKeyPair() throws Exception {
        return ResultVO.success("数据获取成功", EncryptUtils.generateKeyPair());
    }


}
