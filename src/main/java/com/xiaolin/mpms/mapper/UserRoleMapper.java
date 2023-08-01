package com.xiaolin.mpms.mapper;

import com.xiaolin.mpms.entity.user.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户角色关联表 Mapper 接口
 * </p>
 *
 * @author xiaolin
 * @since 2023-04-11
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    Integer deleteRoleIdByUid(@Param("uid") Integer uid);
    Integer insertRoleId(@Param("uid") Integer uid,@Param("roleIds") List<Integer> roleIds);
    Integer deleteMenuIdByRid(@Param("rid") Integer roleId);
    Integer insertMenuId(@Param("rid") Integer roleId,@Param("menuIds") List<Integer> menuIds);
}
