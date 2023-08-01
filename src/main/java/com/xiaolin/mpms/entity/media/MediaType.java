/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity.media;

import com.baomidou.mybatisplus.annotation.*;
import com.xiaolin.mpms.entity.common.BaseEntry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 文件类型表
 * </p>
 *
 * @author xiaolin
 * @since 2023-03-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("xl_media_type")
@ApiModel(value = "MediaType对象", description = "文件类型表")
public class MediaType extends BaseEntry implements Serializable {

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
    private MediaType parent;

    public MediaType(Integer pid, String name, String title) {
        this.pid = pid;
        this.name = name;
        this.title = title;
    }
}
