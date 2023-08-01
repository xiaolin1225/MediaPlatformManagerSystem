/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity.content;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaolin.mpms.entity.common.BaseEntry;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiaolin
 * @since 2023-05-12
 */
@Getter
@Setter
@TableName("xl_content_post_result")
@ApiModel(value = "ContentPostResult对象", description = "")
public class ContentPostResult extends BaseEntry {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("发布ID")
    private Integer postId;

    @ApiModelProperty("平台ID")
    private Integer platformId;

    @ApiModelProperty("发布时间")
    private LocalDateTime postTime;

    @ApiModelProperty("发布结果")
    private String postResult;

    @ApiModelProperty("最终发布结果")
    private String finalResult;

    @ApiModelProperty("排序")
    private Integer order;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
