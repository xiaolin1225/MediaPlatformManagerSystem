/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.mpms.mapper.MediaMetaMapper;
import com.xiaolin.mpms.service.FileMetaService;
import com.xiaolin.mpms.entity.media.MediaMeta;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文件元数据服务实现类
 * </p>
 *
 * @author xiaolin
 * @since 2023-02-28
 */
@Service
public class FileMetaServiceImpl extends ServiceImpl<MediaMetaMapper, MediaMeta> implements FileMetaService {

}
