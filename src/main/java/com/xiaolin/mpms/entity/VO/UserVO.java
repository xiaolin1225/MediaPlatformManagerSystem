/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity.VO;

import com.xiaolin.mpms.entity.user.Department;
import com.xiaolin.mpms.entity.user.Position;
import com.xiaolin.mpms.entity.user.Role;
import com.xiaolin.mpms.entity.user.User;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@ApiModel(value = "用户传输类", description = "用户信息")
public class UserVO extends User {

    private static final long serialVersionUID = 1L;

    private List<Integer> roleIds;

    private List<Role> roles;

    private List<String> permissions;

    private Department department;

    private Position position;

    public UserVO() {
    }

    public UserVO(User user) {
        super(user.getId(), user.getUid(), user.getUsername(), null, user.getInitialPassword(), user.getNickname(), user.getAvatar(), user.getEmail(), user.getPhone(), user.getDid(), user.getPid(), user.getIsEnable(), user.getOrder(), user.getStatus(), user.getIsDeleted(), user.getCreateTime(), user.getUpdateTime(), user.getVersion());
    }
}
