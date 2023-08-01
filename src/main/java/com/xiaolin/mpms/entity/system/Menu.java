/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity.system;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaolin.mpms.entity.common.BaseEntry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("xl_menu")
@ApiModel(value = "Menu对象", description = "")
public class Menu extends BaseEntry {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("父级ID")
    private Integer pid;

    @ApiModelProperty("子菜单")
    @TableField(exist = false)
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

    @ApiModelProperty("权限标识")
    private String permission;

    @ApiModelProperty("是否导航菜单")
    private Boolean isNav;

    @ApiModelProperty("菜单类型")
    private String type;

    @ApiModelProperty("是否显示页头")
    private Boolean isShowHeader;

    public Menu(Integer id, Integer pid, List<Menu> children, String title, String icon, String name, String path, String redirect, String component, String remark, String permission, Boolean isNav, String type, Boolean isShowHeader, Integer order, Integer status, Boolean isDeleted, LocalDateTime createTime, LocalDateTime updateTime, Integer version) {
        super(order, status, isDeleted, createTime, updateTime, version);
        this.id = id;
        this.pid = pid;
        this.children = children;
        this.title = title;
        this.icon = icon;
        this.name = name;
        this.path = path;
        this.redirect = redirect;
        this.component = component;
        this.remark = remark;
        this.permission = permission;
        this.isNav = isNav;
        this.type = type;
        this.isShowHeader = isShowHeader;
    }

    public Menu(Integer id, Integer pid, List<Menu> children, String title, String icon, String name, String path, String redirect, String component, String remark, String permission, Boolean isNav, String type, Boolean isShowHeader) {
        this.id = id;
        this.pid = pid;
        this.children = children;
        this.title = title;
        this.icon = icon;
        this.name = name;
        this.path = path;
        this.redirect = redirect;
        this.component = component;
        this.remark = remark;
        this.permission = permission;
        this.isNav = isNav;
        this.type = type;
        this.isShowHeader = isShowHeader;
    }
}
