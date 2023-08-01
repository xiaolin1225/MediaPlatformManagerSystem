package com.xiaolin.mpms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.mpms.entity.Platform.Platform;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 平台信息 服务类
 * </p>
 *
 * @author xiaolin
 * @since 2023-05-11
 */
public interface PlatformService extends IService<Platform> {

    Page<Platform> getListPage(Integer current, Integer size, Map<String, String> filter);

    Boolean saveInfo(Platform platform);

    Platform getPlatformInfo(Integer id);

    Boolean updatePlatformInfo(Platform platform);

    Boolean deletePlatformInfo(Integer id);

    List<Platform> getSelectList();
}
