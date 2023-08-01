/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.xiaolin.mpms.entity.VO.ResultVO;
import com.xiaolin.mpms.entity.media.MediaType;
import com.xiaolin.mpms.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("config")
public class ConfigController {

    @Autowired
    private MediaService mediaService;

    @GetMapping("media-type")
    public ResultVO<List<MediaType>> getTopMediaType(){
        return ResultVO.success("数据获取成功",mediaService.getTopMediaType());
    }
}
