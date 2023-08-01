/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity.content;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaolin.mpms.entity.common.BaseEntry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
@TableName("xl_content_check")
@ApiModel(value = "内容审核", description = "")
public class ContentCheck extends BaseEntry {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("内容ID")
    private String contentId;

    @ApiModelProperty("内容标题")
    private String contentTitle;

    @ApiModelProperty("内容")
    @TableField(exist = false)
    private Content content;

    @ApiModelProperty("审核进程")
    private String process;

    @ApiModelProperty("当前审核索引")
    private Integer processIndex;

    @ApiModelProperty("审核节点个数")
    private Integer processTotal;

    @ApiModelProperty("提交审核人员ID")
    private String uid;

    @ApiModelProperty("是否允许审核")
    @TableField(exist = false)
    private Boolean enableCheck;

    @ApiModelProperty("是否创建人员")
    @TableField(exist = false)
    private Boolean isCreateUser;

    @ApiModelProperty("审核人员列表")
    @TableField(exist = false)
    private List<ContentCheckUser> checkUsers;

    @ApiModelProperty("提交版本")
    private Integer checkVersion;
}
