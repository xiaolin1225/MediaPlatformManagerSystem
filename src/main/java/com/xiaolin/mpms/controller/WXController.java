/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.xiaolin.mpms.entity.Platform.WX.PublishResponse;
import com.xiaolin.mpms.entity.VO.ResultVO;
import com.xiaolin.mpms.service.WxService;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("wx")
@Log4j2
public class WXController {

    //    @RequestMapping(value = "")
//    public Object checkSignature(Map<Object, Object> data, String signature, String timestamp, String nonce, String echostr) {
//        log.info("xmlData=>{},data=>{},signature=>{},timestamp=>{},nonce=>{},echostr=>{}", xmlData, data, signature, timestamp, nonce, echostr);
//        return data;
//    }

    @Autowired
    private WxService wxService;

    @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public String checkSignature(@RequestParam(required = false) Map<String, Object> data, @RequestBody(required = false) PublishResponse xmlData) {
        log.info("data=>{},xmlData=>{}", data, xmlData);
        if (data != null) {
            if (data.containsKey("echostr")) {
                return (String) data.get("echostr");
            }
            return "";
        } else {
            wxService.testApi("get_api_domain_ip");
            return ResultVO.success("数据获取成功", xmlData).toString();
        }
    }

//    @RequestMapping(value = "", consumes = {MediaType.APPLICATION_XML_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResultVO<PublishResponse> allDateResponse(@RequestBody PublishResponse data) {
//        log.info("data=>{}", data);
//        return ResultVO.success(data);
//    }
}
