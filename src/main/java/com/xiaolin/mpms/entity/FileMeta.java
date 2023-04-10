/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
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
 * 文件元数据表
 * </p>
 *
 * @author xiaolin
 * @since 2023-02-28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("xl_file_meta")
@ApiModel(value = "FileMeta对象", description = "文件元数据表")
public class FileMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("文件ID")
    private Integer fid;

    @ApiModelProperty("标签")
    private String label;

    @ApiModelProperty("数据")
    private String value;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("排序")
    private Integer sort;

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


    public FileMeta(Integer fid, String label, Object value) {
        this.fid = fid;
        this.label = label;
        this.value = JSON.toJSONString(value);
    }

    public FileMeta(String label, Object value) {
        this.label = label;
        this.value = JSON.toJSONString(value);
    }

    public FileMeta(String label, Object value, Integer status) {
        this.label = label;
        this.value = JSON.toJSONString(value);
        this.status = status;
    }

    public FileMeta(String label, Object value, Integer status, Integer sort) {
        this.label = label;
        this.value = JSON.toJSONString(value);
        this.status = status;
        this.sort = sort;
    }
}
