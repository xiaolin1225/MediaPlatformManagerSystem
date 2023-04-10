/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 角色菜单关联表
 * </p>
 *
 * @author xiaolin
 * @since 2023-03-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("xl_role_menu")
@ApiModel(value = "RoleMenu对象", description = "角色菜单关联表")
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色ID")
    private Integer rid;

    @ApiModelProperty("菜单ID")
    private Integer mid;
}
