/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

/*
 * 微信公众号（小林今天努力了）：AppID->wx3eb89c91573749ec;AppSecret->6274d3fbe2b5f24accb4b89ef4f6e52b
 */

package com.xiaolin.mpms.entity.Platform;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaolin.mpms.entity.common.BaseEntry;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 平台信息
 * </p>
 *
 * @author xiaolin
 * @since 2023-05-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("xl_platform")
@ApiModel(value = "Platform对象", description = "平台信息")
public class Platform extends BaseEntry {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("平台名称")
    private String title;

    @ApiModelProperty("平台类型")
    private String type;

    @ApiModelProperty("平台状态")
    private Integer platformStatus;

    @ApiModelProperty("是否认证")
    private Boolean isAuthentication;

    @ApiModelProperty("认证数据")
    private String authenticationData;
}
