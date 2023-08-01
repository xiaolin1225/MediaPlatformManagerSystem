/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity.system;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 日志
 * </p>
 *
 * @author xiaolin
 * @since 2023-04-14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("xl_log")
@ApiModel(value = "Log对象", description = "日志")
public class SystemLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("模块")
    private String title;

    @ApiModelProperty("操作类型")
    private Integer operationType;

    @ApiModelProperty("方法名称")
    private String method;

    @ApiModelProperty("请求方式")
    private String requestMethod;

    @ApiModelProperty("日志类型")
    @JsonIgnore
    private Integer logType;

    @ApiModelProperty("操作用户")
    private String operationUser;

    @ApiModelProperty("请求地址")
    private String operationUrl;

    @ApiModelProperty("请求IP")
    private String operationIp;

    @ApiModelProperty("操作地点")
    private String operationLocation;

    @ApiModelProperty("请求参数")
    @JsonIgnore
    private String operationParam;

    @ApiModelProperty("返回结果")
    @JsonIgnore
    private String operationResult;

    @ApiModelProperty("操作信息")
    private String operationMessage;

    @ApiModelProperty("错误信息")
    private String errorMessage;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("耗费时间")
    private Long costTime;

    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("版本号")
    @JsonIgnore
    private Integer version;
}
