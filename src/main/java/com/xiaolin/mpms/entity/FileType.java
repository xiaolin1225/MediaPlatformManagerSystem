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
 * 文件类型表
 * </p>
 *
 * @author xiaolin
 * @since 2023-03-01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("xl_file_type")
@ApiModel(value = "FileType对象", description = "文件类型表")
public class FileType implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("父级ID")
    private Integer pid;

    @ApiModelProperty("类型")
    private String name;

    @ApiModelProperty("类型标题")
    private String title;

    @ApiModelProperty("父级类型")
    @TableField(exist = false)
    private FileType parent;

    @ApiModelProperty("状态")
    private Integer status;

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

    public FileType(Integer pid, String name, String title) {
        this.pid = pid;
        this.name = name;
        this.title = title;
    }
}
