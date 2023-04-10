package com.xiaolin.mpms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaolin.mpms.entity.FileType;

/**
 * <p>
 * 文件类型表 Mapper 接口
 * </p>
 *
 * @author xiaolin
 * @since 2023-03-01
 */
public interface FileTypeMapper extends BaseMapper<FileType> {

    FileType getFileTypeByName(String name);
}
