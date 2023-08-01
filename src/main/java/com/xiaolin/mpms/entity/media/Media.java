/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity.media;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaolin.mpms.entity.common.BaseEntry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * <p>
 * 文件表
 * </p>
 *
 * @author xiaolin
 * @since 2023-02-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("xl_media")
@ApiModel(value = "Media对象", description = "文件表")
public class Media extends BaseEntry {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("替换文字")
    private String alt;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("服务器存储名称")
    private String serverName;

    @ApiModelProperty("相对路径")
    private String path;

    @ApiModelProperty("完整路径")
    @JsonIgnore
    private String fullPath;

    @ApiModelProperty("媒体链接")
    @TableField(exist = false)
    private String url;

    @ApiModelProperty("分类ID")
    private Integer cid;

    @ApiModelProperty("封面路径")
    private String thumb;

    @ApiModelProperty("媒体类型")
    private String type;

    @ApiModelProperty("后缀名")
    private String suffix;

    @ApiModelProperty("文件大小")
    private Object size;

    @ApiModelProperty("元数据")
    @TableField(exist = false)
    private List<MediaMeta> meta;
}
