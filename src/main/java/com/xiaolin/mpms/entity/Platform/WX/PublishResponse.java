/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity.Platform.WX;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "xml")
public class PublishResponse {

//    signature	微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
//    timestamp	时间戳
//    nonce	随机数
//    echostr	随机字符串

//    @JacksonXmlProperty(localName = "signature")
//    private String signature;
//    @JacksonXmlProperty(localName = "timestamp")
//    private String timestamp;
//    @JacksonXmlProperty(localName = "nonce")
//    private String nonce;
//    @JacksonXmlProperty(localName = "echostr")
//    private String echostr;

    /*
     * 参数	说明
     * ToUserName	公众号的ghid
     * FromUserName	公众号群发助手的openid，为mphelper
     * CreateTime	创建时间的时间戳
     * MsgType	消息类型，此处为event
     * Event	事件信息，此处为PUBLISHJOBFINISH
     * publish_id	发布任务id
     * publish_status	发布状态，0:成功, 1:发布中，2:原创失败, 3: 常规失败, 4:平台审核不通过, 5:成功后用户删除所有文章, 6: 成功后系统封禁所有文章
     * article_id	当发布状态为0时（即成功）时，返回图文的 article_id，可用于“客服消息”场景
     * count	当发布状态为0时（即成功）时，返回文章数量
     * idx	当发布状态为0时（即成功）时，返回文章对应的编号
     * article_url	当发布状态为0时（即成功）时，返回图文的永久链接
     * fail_idx	当发布状态为2或4时，返回不通过的文章编号，第一篇为 1；其他发布状态则为空
     */

    @JacksonXmlProperty(localName = "ToUserName")
    private String toUserName;
    @JacksonXmlProperty(localName = "FromUserName")
    private String fromUserName;
    @JacksonXmlProperty(localName = "CreateTime")
    private String createTime;
    @JacksonXmlProperty(localName = "MsgType")
    private String msgType;
    @JacksonXmlProperty(localName = "Event")
    private String event;

    @JacksonXmlProperty(localName = "PublishEventInfo")
    private PublishEventInfo publishEventInfo;
}
