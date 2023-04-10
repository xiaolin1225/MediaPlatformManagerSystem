/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.mpms.mapper.FileTypeMapper;
import com.xiaolin.mpms.service.FileTypeService;
import com.xiaolin.mpms.entity.FileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文件类型表 服务实现类
 * </p>
 *
 * @author xiaolin
 * @since 2023-03-01
 */
@Service
public class FileTypeServiceImpl extends ServiceImpl<FileTypeMapper, FileType> implements FileTypeService {

    @Autowired
    private FileTypeMapper fileTypeMapper;

    /**
     * 根据文件类型名获取文件类型
     *
     * @param name 文件类型名
     * @return 文件类型
     */
    @Override
    public FileType getFileTypeByName(String name) {
        return fileTypeMapper.getFileTypeByName(name);
    }
}
