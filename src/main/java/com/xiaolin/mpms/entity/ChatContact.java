/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiaolin
 * @since 2023-04-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("xl_chat_contact")
@ApiModel(value = "ChatContact对象", description = "")
public class ChatContact implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户UID")
    private String uid;

    @ApiModelProperty("联系人UID")
    private String contactUid;

    @ApiModelProperty("联系人信息")
    @TableField(exist = false)
    private User contact;

    @ApiModelProperty("是否删除")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
