/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.mapper;

import com.xiaolin.mpms.entity.ChatMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {

    List<ChatMessage> getMessageList(@Param("uid") String uid,@Param("contactUid") String contactUid,@Param("column") List<String> column);
}
