/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity.chat;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaolin.mpms.entity.user.User;
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
@TableName("xl_chat_message")
@ApiModel(value = "ChatMessage对象", description = "")
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("发送人uid")
    @TableField("`from`")
    private String from;

    @ApiModelProperty("发送人")
    @TableField(exist = false)
    private User fromUser;

    @ApiModelProperty("接收人uid")
    @TableField("`to`")
    private String to;

    @ApiModelProperty("接收人")
    @TableField(exist = false)
    private User toUser;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty("乐观锁")
    @Version
    private Integer version;
}
