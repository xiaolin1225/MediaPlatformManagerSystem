/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.mpms.entity.FileType;

/**
 * <p>
 * 文件类型表 服务类
 * </p>
 *
 * @author xiaolin
 * @since 2023-03-01
 */
public interface FileTypeService extends IService<FileType> {

    /**
     * 根据文件类型名获取文件类型
     *
     * @param name 文件类型名
     * @return 文件类型
     */
    FileType getFileTypeByName(String name);
}
