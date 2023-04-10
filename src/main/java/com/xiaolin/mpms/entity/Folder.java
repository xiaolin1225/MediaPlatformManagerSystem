/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 文件表
 * </p>
 *
 * @author xiaolin
 * @since 2023-02-28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("xl_folder")
@ApiModel(value = "Folder对象", description = "文件表")
public class Folder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("文件夹标题")
    private String title;

    @ApiModelProperty("替换文字")
    private String alt;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("文件夹相对路径")
    private String path;

    @ApiModelProperty("文件夹完整路径")
    private String fullPath;

    @ApiModelProperty("父级文件夹ID")
    private Integer folderId;

    @ApiModelProperty("封面路径")
    private String thumb;

    @ApiModelProperty("文件大小")
    private Object size;

    @ApiModelProperty("存储方式")
    private String storageType;

    @ApiModelProperty("文件状态")
    private Integer status;

    @ApiModelProperty("是否可用")
    private Integer isEnable;

    @ApiModelProperty("是否删除")
    @TableLogic
    @JsonIgnore
    private Integer isDelete;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty("乐观锁")
    @Version
    @JsonIgnore
    private Integer version;
}
