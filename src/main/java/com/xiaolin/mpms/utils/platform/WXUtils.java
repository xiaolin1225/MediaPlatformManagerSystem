/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.utils.platform;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.sun.media.jfxmedia.MediaException;
import com.xiaolin.mpms.entity.Platform.WX.WXData;
import com.xiaolin.mpms.entity.content.Content;
import com.xiaolin.mpms.exception.ContentException;
import com.xiaolin.mpms.service.MediaService;
import com.xiaolin.mpms.utils.FileUtil;
import com.xiaolin.mpms.utils.http.HttpUtils;
import com.xiaolin.mpms.utils.spring.SpringUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.security.MessageDigest;
import java.util.*;

@Log4j2
public class WXUtils {
    /**
     * 用SHA1算法生成安全签名
     *
     * @param token     票据
     * @param timestamp 时间戳
     * @param nonce     随机字符串
     * @param encrypt   密文
     * @return 安全签名
     * @throws AesException 异常
     */
    public static String getSHA1(String token, String timestamp, String nonce, String encrypt) throws AesException {
        try {
            String[] array = new String[]{token, timestamp, nonce, encrypt};
            StringBuilder sb = new StringBuilder();
            // 字符串排序
            Arrays.sort(array);
            for (int i = 0; i < 4; i++) {
                sb.append(array[i]);
            }
            String str = sb.toString();
            // SHA1签名生成
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] digest = md.digest();

            StringBuilder hexStr = new StringBuilder();
            String shaHex = "";
            for (byte b : digest) {
                shaHex = Integer.toHexString(b & 0xFF);
                if (shaHex.length() < 2) {
                    hexStr.append(0);
                }
                hexStr.append(shaHex);
            }
            return hexStr.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AesException(AesException.ComputeSignatureError);
        }
    }

    //    @Value("${platform.wx.api}")
    private static final String baseUrl = "https://api.weixin.qq.com";

    /**
     * 获取AccessToken
     *
     * @param appId
     * @param secret
     * @return
     */
    public static WXTokenData getAccessToken(String appId, String secret) {
        WXTokenData wxTokenData = new WXTokenData(appId, secret);
        String res = HttpUtils.sendPost(baseUrl + "/cgi-bin/stable_token", JSON.toJSONString(wxTokenData));
        return JSON.parseObject(res, WXTokenData.class);
    }

    /**
     * 新增素材
     */
    public static WXMediaData uploadMedia(String access_token, String type, String path) {
        MediaService mediaService = SpringUtils.getBean(MediaService.class);
//        String realPath = mediaService.getRealPath(path);
        File file = new File(path);
        log.info("filepath=>{}", path);
        if (!file.exists()) {
            throw new MediaException("文件不存在");
        }
        try {

            String url = baseUrl + "/cgi-bin/material/add_material?access_token=" + access_token + "&type=" + type;
            //封装请求头
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE + ";charset=UTF-8");

            //文件处理
            FileSystemResource resource = new FileSystemResource(file);

            //表单处理
            MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
            form.add("media", resource);
            HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, httpHeaders);
//
            RestTemplate restTemplate = new RestTemplate();
//            //发送请求
            return JSON.parseObject(restTemplate.postForObject(url, files, String.class), WXMediaData.class);
        } catch (Exception e) {
            throw new ContentException("文件上失败");
        }
    }

    public static WXMediaData addDraft(String access_token, List<Content> contentList) {
        List<WXArticle> wxArticles = new ArrayList<>();
        for (Content content : contentList) {
            WXArticle wxArticle = new WXArticle();
            String path = FileUtil.getServerPath() + content.getThumb();
            WXMediaData image = uploadMedia(access_token, "image", path);
            wxArticle.setThumb_media_id(image.getMedia_id());
            log.info("{}=>{}", content.getTitle(), image.getMedia_id());
            wxArticle.setTitle(content.getTitle());
            wxArticle.setAuthor(content.getAuthor());
            wxArticle.setDigest(content.getSummary());
            wxArticle.setContent(content.getContent());
            if (content.getSourceEnable()) {
                wxArticle.setContent_source_url(content.getSource());
            }
            wxArticle.setNeed_open_comment(content.getCommit() ? 1 : 0);
            wxArticles.add(wxArticle);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("articles", wxArticles);

        String s = HttpUtils.sendPost(baseUrl + "/cgi-bin/draft/add?access_token=" + access_token, jsonObject.toJSONString());
        return JSON.parseObject(s, WXMediaData.class);
    }

    public static WXBaseMsg freePublish(String access_token, String media_id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("media_id", media_id);
        String s = HttpUtils.sendPost(baseUrl + "/cgi-bin/freepublish/submit?access_token=" + access_token, jsonObject.toJSONString());
        return JSON.parseObject(s, WXBaseMsg.class);
    }

    public static WXData getWXData(String access_token, String type, Map<String, Object> data) {
        String url = "";
        String method = "GET";
        switch (type) {
            case "Api Domain IP":
                url = "/cgi-bin/get_api_domain_ip";
                break;
            case "Callback IP":
                url = "/cgi-bin/getcallbackip";
                break;
            case "User List":
                url = "/cgi-bin/user/get";
        }
        String s = "";
        if (method.equals("GET")){
            s = HttpUtils.sendPost(baseUrl + url + "?access_token=" + access_token, JSON.toJSONString(data));
        }else {
            s = HttpUtils.sendPost(baseUrl + url + "?access_token=" + access_token, JSON.toJSONString(data));
        }
        return JSON.parseObject(s, WXData.class);
    }

}