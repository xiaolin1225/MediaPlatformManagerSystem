/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaolin.mpms.entity.common.BaseEntry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * <p>
 * 部门
 * </p>
 *
 * @author xiaolin
 * @since 2023-04-11
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("xl_department")
@ApiModel(value = "部门", description = "部门")
public class Department extends BaseEntry {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("父级ID")
    private Integer pid;

    @ApiModelProperty("祖级列表")
    private String ancestors;

    @ApiModelProperty("部门名称")
    @TableField("`name`")
    private String name;

    @ApiModelProperty("排序")
    @TableField("`order`")
    private Integer order;

    @ApiModelProperty("负责人")
    private String leader;

    @ApiModelProperty("子部门")
    @TableField(exist = false)
    private List<Department> children;
}
