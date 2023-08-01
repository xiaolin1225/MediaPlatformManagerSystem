/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.xiaolin.mpms.entity.VO.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
@Api(tags = "首页")
public class HomeController {

    @GetMapping("index")
    @ApiOperation(value = "获取首页数据")
    @ApiResponses({
            @ApiResponse(code = 200, message = "数据获取成功")
    })
    public ResultVO backHome() {
        return ResultVO.success("数据获取成功", null);
    }
}
