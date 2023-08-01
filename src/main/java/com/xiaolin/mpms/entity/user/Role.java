/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity.user;

import com.baomidou.mybatisplus.annotation.*;
import com.xiaolin.mpms.entity.common.BaseEntry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户角色关联表
 * </p>
 *
 * @author xiaolin
 * @since 2023-03-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("xl_role")
@ApiModel(value = "Role对象", description = "用户角色关联表")
public class Role extends BaseEntry {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("角色名称")
    @TableField("`name`")
    private String name;

    @ApiModelProperty("角色标识")
    @TableField("`key`")
    private String key;

    @ApiModelProperty("权限菜单ID")
    @TableField(exist = false)
    private List<Integer> menuIds;

    public Role(Integer id, String name, String key, Integer order, Integer status, Boolean isDeleted, LocalDateTime createTime, LocalDateTime updateTime, Integer version) {
        super(order, status, isDeleted, createTime, updateTime, version);
        this.id = id;
        this.name = name;
        this.key = key;
    }
}
