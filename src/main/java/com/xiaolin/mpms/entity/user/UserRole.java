/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity.user;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 用户角色关联表
 * </p>
 *
 * @author xiaolin
 * @since 2023-04-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("xl_user_role")
@ApiModel(value = "UserRole对象", description = "用户角色关联表")
public class UserRole {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户ID")
    private Integer uid;

    @ApiModelProperty("角色ID")
    private Integer rid;

//    public UserRole(Integer uid, Integer rid) {
//        this.uid = uid;
//        this.rid = rid;
//    }
}
