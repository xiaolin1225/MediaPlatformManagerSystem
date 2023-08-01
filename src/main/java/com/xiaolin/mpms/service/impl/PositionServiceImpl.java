/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.mpms.entity.user.Position;
import com.xiaolin.mpms.entity.user.User;
import com.xiaolin.mpms.exception.PositionException;
import com.xiaolin.mpms.mapper.PositionMapper;
import com.xiaolin.mpms.mapper.UserMapper;
import com.xiaolin.mpms.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 职位 服务实现类
 * </p>
 *
 * @author xiaolin
 * @since 2023-04-11
 */
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements PositionService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Position> getList() {
        return list();
    }

    @Override
    public Page<Position> getListPage(int current, int size, Map<String, String> filter) {
        String status = filter.getOrDefault("status", "");
        String keyword = filter.getOrDefault("keyword", "");
        QueryWrapper<Position> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("`name`", keyword);
        if (!status.equals("")) {
            queryWrapper.eq("status", status);
        }
        Page<Position> page = new Page<>(current, size);
        return page(page, queryWrapper);
    }

    @Override
    public Boolean savePositionInfo(Position position) {
        Position sameName = getOne(new QueryWrapper<Position>().eq("`name`", position.getName()));
        if (sameName != null && !sameName.getId().equals(position.getId())) {
            throw new PositionException(4031, "职位已存在");
        }
        Position sameKey = getOne(new QueryWrapper<Position>().eq("`key`", position.getKey()));
        if (sameKey != null && !sameKey.getId().equals(position.getId())) {
            throw new PositionException(4032, "职位标识不能重复");
        }
        position.setKey(position.getKey().toUpperCase());
        return saveOrUpdate(position);
    }

    @Override
    public Boolean deletePosition(Integer id) {
        if (userMapper.selectCount(new QueryWrapper<User>().eq("pid", id)) > 0) {
            throw new PositionException(4033, "该职位已分配给用户使用，禁止删除！");
        }
        return removeById(id);
    }

    @Override
    public Boolean deletePositions(List<Integer> ids) {
        int size = ids.size();
        int count = 0;
        for (Integer id : ids) {
            if (deletePosition(id)) {
                count++;
            }
        }
        return size == count;
    }
}
