/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity.Platform.WX;

public class Draft {
    /*
        title	是	标题
        author	否	作者
        digest	否	图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空。如果本字段为没有填写，则默认抓取正文前54个字。
        content	是	图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS,涉及图片url必须来源 "上传图文消息内的图片获取URL"接口获取。外部图片url将被过滤。
        content_source_url	否	图文消息的原文地址，即点击“阅读原文”后的URL
        thumb_media_id	是	图文消息的封面图片素材id（必须是永久MediaID）
        need_open_comment	否	Uint32 是否打开评论，0不打开(默认)，1打开
        only_fans_can_comment	否	Uint32 是否粉丝才可评论，0所有人可评论(默认)，1粉丝才可评论
     */
    private String title;
    private String author;
    private String digest;

    private String content;

    private String content_source_url;

    private String thumb_media_id;

    private Integer need_open_comment;

    private Integer only_fans_can_comment;
}
