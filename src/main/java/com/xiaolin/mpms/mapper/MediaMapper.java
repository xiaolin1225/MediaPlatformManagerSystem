/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaolin.mpms.entity.media.Media;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文件表 Mapper 接口
 * </p>
 *
 * @author xiaolin
 * @since 2023-02-28
 */
public interface MediaMapper extends BaseMapper<Media> {

    int getFileTotalNumWithPath(@Param("path") String path, @Param("type") String type);

    List<Media> getFileListByPath(@Param("path") String path, @Param("type") String type, @Param("start") int start, @Param("size") int size);

    int getFileTotalNumWithPid(@Param("folderId") Integer fid, @Param("type") String type);

    List<Media> getFileListByFid(@Param("folderId") Integer fid, @Param("type") String type, @Param("start") int start, @Param("size") int size);

    Media getMediaById(@Param("id") Integer id);
}
