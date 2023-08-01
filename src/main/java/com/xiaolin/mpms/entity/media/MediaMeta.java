/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity.media;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaolin.mpms.entity.common.BaseEntry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 文件元数据表
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
@TableName("xl_media_meta")
@ApiModel(value = "MediaMeta对象", description = "文件元数据表")
public class MediaMeta extends BaseEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("媒体ID")
    private Integer mid;

    @ApiModelProperty("标签")
    private String label;

    @ApiModelProperty("数据")
    private String value;

    public MediaMeta(Integer mid, String label, Object value) {
        this.mid = mid;
        this.label = label;
        this.value = JSON.toJSONString(value);
    }

    public MediaMeta(String label, Object value) {
        this.label = label;
        this.value = JSON.toJSONString(value);
    }

    public MediaMeta(String label, Object value, Integer status) {
        this.label = label;
        this.value = JSON.toJSONString(value);
        super.setStatus(status);
    }

    public MediaMeta(String label, Object value, Integer status, Integer order) {
        this.label = label;
        this.value = JSON.toJSONString(value);
        super.setStatus(status);
        super.setOrder(order);
    }
}
