/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaolin.mpms.entity.common.BaseEntry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户实体类
 * </p>
 *
 * @author xiaolin
 * @since 2023-02-26
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("xl_user")
@ApiModel(value = "用户实体类", description = "用户信息")
public class User extends BaseEntry {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "序号", required = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户ID", required = true)
    private String uid;

    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @ApiModelProperty(value = "密码")
    @JsonIgnore
    private String password;

    @ApiModelProperty(value = "初始密码")
    private String initialPassword;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "电子邮箱")
    private String email;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "部门ID")
    private Integer did;

    @ApiModelProperty(value = "职位ID")
    private Integer pid;

    @ApiModelProperty(value = "是否启用")
    private Boolean isEnable;

    public User(Integer id, String uid, String username, String password, String initialPassword, String nickname, String avatar, String email, String phone, Integer did, Integer pid, Boolean isEnable, Integer order, Integer status, Boolean isDeleted, LocalDateTime createTime, LocalDateTime updateTime, Integer version) {
        super(order, status, isDeleted, createTime, updateTime, version);
        this.id = id;
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.initialPassword = initialPassword;
        this.nickname = nickname;
        this.avatar = avatar;
        this.email = email;
        this.phone = phone;
        this.did = did;
        this.pid = pid;
        this.isEnable = isEnable;
    }

    public User(Integer id, String uid, String username, String password, String initialPassword, String nickname, String avatar, String email, String phone, Integer did, Integer pid, Boolean isEnable) {
        this.id = id;
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.initialPassword = initialPassword;
        this.nickname = nickname;
        this.avatar = avatar;
        this.email = email;
        this.phone = phone;
        this.did = did;
        this.pid = pid;
        this.isEnable = isEnable;
    }
}
