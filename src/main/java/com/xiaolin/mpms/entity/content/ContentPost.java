/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity.content;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaolin.mpms.entity.common.BaseEntry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author xiaolin
 * @since 2023-03-18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("xl_content_post")
@ApiModel(value = "内容发布", description = "")
public class ContentPost extends BaseEntry {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("内容ID")
    private String contentId;

    @ApiModelProperty("内容标题")
    private String contentTitle;

    @ApiModelProperty("内容")
    @TableField(exist = false)
    private Content content;

    @ApiModelProperty("发布时间")
    private LocalDateTime postTime;

    @ApiModelProperty("发布平台")
    private String postPlatform;

    @ApiModelProperty("发布结果")
    private String postResult;
}
