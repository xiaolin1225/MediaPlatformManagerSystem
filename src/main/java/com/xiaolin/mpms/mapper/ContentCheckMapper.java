package com.xiaolin.mpms.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.mpms.entity.content.ContentCheck;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaolin
 * @since 2023-05-10
 */
public interface ContentCheckMapper extends BaseMapper<ContentCheck> {
    Page<ContentCheck> getListPage(Page<ContentCheck> page, @Param("ew") Wrapper<ContentCheck> wrapper);

    ContentCheck getCheck(@Param("ew") Wrapper<ContentCheck> wrapper);
}
