/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaolin.mpms.entity.FileInfo;
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
public interface FileMapper extends BaseMapper<FileInfo> {

    int getFileTotalNumWithPath(@Param("path") String path, @Param("type") String type);

    List<FileInfo> getFileListByPath(@Param("path") String path, @Param("type") String type, @Param("start") int start, @Param("size") int size);

    int getFileTotalNumWithPid(@Param("folderId") Integer fid, @Param("type") String type);

    List<FileInfo> getFileListByFid(@Param("folderId") Integer fid, @Param("type") String type, @Param("start") int start, @Param("size") int size);

    FileInfo getFileById(@Param("id") Integer id);
}
