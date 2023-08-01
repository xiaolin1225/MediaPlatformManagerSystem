/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.mapper;

import com.xiaolin.mpms.entity.chat.ChatContact;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaolin.mpms.entity.user.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaolin
 * @since 2023-04-01
 */
public interface ChatContactMapper extends BaseMapper<ChatContact> {
    List<User> selectListByUid(@Param("uid") String uid,@Param("column") List<String> column);
}
