package com.xiaolin.mpms.mapper;

import com.xiaolin.mpms.entity.content.ContentCheckUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xiaolin
 * @since 2023-05-10
 */
public interface ContentCheckUserMapper extends BaseMapper<ContentCheckUser> {

    List<ContentCheckUser> getCheckUserListByCid(@Param("cid") Integer cid);

    Boolean updateStatusByUid(@Param("uid") String uid);
}
