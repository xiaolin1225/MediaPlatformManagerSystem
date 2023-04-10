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
@AllArgsConstructor
@NoArgsConstructor
@TableName("xl_file")
@ApiModel(value = "File对象", description = "文件表")
public class FileInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("文件标题")
    private String title;

    @ApiModelProperty("替换文字")
    private String alt;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("服务器存储文件名称")
    private String serverName;

    @ApiModelProperty("文件相对路径")
    private String path;

    @ApiModelProperty("文件完整路径")
    @JsonIgnore
    private String fullPath;

    @ApiModelProperty("文件MD5")
    @JsonIgnore
    private String md5;

    @ApiModelProperty("文件链接")
    @TableField(exist = false)
    private String url;

    @ApiModelProperty("文件夹ID")
    private Integer folderId;

    @ApiModelProperty("封面路径")
    private String thumb;

    @ApiModelProperty("文件类型ID")
    @JsonIgnore
    private Integer typeId;

    @ApiModelProperty("文件类型")
    @TableField(exist = false)
    private FileType type;

    @ApiModelProperty("后缀名")
    private String suffix;

    @ApiModelProperty("文件大小")
    private Object size;

    @ApiModelProperty("存储方式")
    private String storageType;

    @ApiModelProperty("是否为文件夹")
    private Boolean isDir;

    @ApiModelProperty("文件状态")
    private Integer status;

    @ApiModelProperty("文件元数据")
    @TableField(exist = false)
    private List<FileMeta> meta;

    @ApiModelProperty("是否可用")
    private Integer isEnable;

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
