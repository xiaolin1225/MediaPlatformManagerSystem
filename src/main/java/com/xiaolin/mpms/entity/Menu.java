/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity;

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
@TableName("xl_menu")
@ApiModel(value = "Menu对象", description = "")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("父级ID")
    private Integer pid;

    @ApiModelProperty("子菜单")
    @TableField(exist = false)
    @JsonIgnore
    private List<Menu> children;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("路径")
    private String path;

    @ApiModelProperty("重定向")
    private String redirect;

    @ApiModelProperty("组件名称")
    private String component;

    @ApiModelProperty("备注")
    @JsonIgnore
    private String remark;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("权限标识")
    private String permission;

    @ApiModelProperty("是否导航菜单")
    private Boolean isNav;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("是否删除")
    @TableLogic
    @JsonIgnore
    private Boolean isDeleted;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty("乐观锁")
    @Version
    @JsonIgnore
    private Integer version;
}
