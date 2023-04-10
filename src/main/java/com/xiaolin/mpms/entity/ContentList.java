/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("xl_content_list")
@ApiModel(value = "内容列表", description = "")
public class ContentList implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("内容ID")
    private String contentId;

    @ApiModelProperty("文章数量")
    private Integer count;

    @ApiModelProperty("创建用户ID")
    private Integer uid;

    @ApiModelProperty("分类ID")
    private Integer cid;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("内容列表")
    @TableField(exist = false)
    private List<Content> contents;

    @ApiModelProperty("发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime postTime;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    public ContentList(String contentId, Integer count, Integer uid, Integer cid) {
        this.contentId = contentId;
        this.count = count;
        this.uid = uid;
        this.cid = cid;
    }
}
