package com.xiaolin.mpms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.mpms.entity.Platform.Platform;
import com.xiaolin.mpms.mapper.PlatformMapper;
import com.xiaolin.mpms.service.PlatformService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 平台信息 服务实现类
 * </p>
 *
 * @author xiaolin
 * @since 2023-05-11
 */
@Service
public class PlatformServiceImpl extends ServiceImpl<PlatformMapper, Platform> implements PlatformService {

    @Override
    public Page<Platform> getListPage(Integer current, Integer size, Map<String, String> filter) {
        LambdaQueryWrapper<Platform> queryWrapper = new LambdaQueryWrapper<>();
        Page<Platform> page = new Page<>(current, size);
        page(page, queryWrapper);
        return page;
    }

    @Override
    public Boolean saveInfo(Platform platform) {
        return save(platform);
    }

    @Override
    public Platform getPlatformInfo(Integer id) {
        LambdaQueryWrapper<Platform> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Platform::getId, id);
        return getOne(queryWrapper);
    }

    @Override
    public Boolean updatePlatformInfo(Platform platform) {
        return updateById(platform);
    }

    @Override
    public Boolean deletePlatformInfo(Integer id) {
        return removeById(id);
    }

    @Override
    public List<Platform> getSelectList() {
        LambdaQueryWrapper<Platform> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Platform::getStatus, 1);
        queryWrapper.select(Platform::getId, Platform::getTitle);
        return list(queryWrapper);
    }
}
