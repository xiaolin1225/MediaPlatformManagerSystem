/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity.content;

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
 *
 * </p>
 *
 * @author xiaolin
 * @since 2023-03-18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("xl_content")
@ApiModel(value = "内容", description = "")
public class Content implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("内容集ID")
    private String contentId;

    @ApiModelProperty("内容ID")
    private String subId;

    @ApiModelProperty("排序")
    private Integer contentIndex;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("封面")
    private String thumb;

    @ApiModelProperty("摘要")
    private String summary;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("来源链接")
    private String source;

    @ApiModelProperty("启用来源链接")
    private Boolean sourceEnable;

    @ApiModelProperty("合集名称")
    @TableField(exist = false)
    private List<ContentTag> collection;

    @ApiModelProperty("启用合集")
    private Boolean collectionEnable;

    @ApiModelProperty("启用原创声明")
    private Boolean original;

    @ApiModelProperty("允许评论")
    private Boolean commit;

    @ApiModelProperty("创建用户ID")
    private Integer uid;

    @ApiModelProperty("分类ID")
    private Integer cid;

    @ApiModelProperty("状态")
    private Integer status;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty("版本")
    @Version
    @JsonIgnore
    private Integer version;
}
