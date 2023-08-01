package com.xiaolin.mpms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.mpms.entity.user.Position;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 职位 服务类
 * </p>
 *
 * @author xiaolin
 * @since 2023-04-11
 */
public interface PositionService extends IService<Position> {
    List<Position> getList();

    Page<Position> getListPage(int current, int size, Map<String, String> filter);

    Boolean savePositionInfo(Position position);

    Boolean deletePosition(Integer id);

    Boolean deletePositions(List<Integer> ids);
}
