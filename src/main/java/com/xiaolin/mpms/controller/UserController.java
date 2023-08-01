/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.mpms.annotation.Log;
import com.xiaolin.mpms.entity.VO.UserVO;
import com.xiaolin.mpms.enums.LogType;
import com.xiaolin.mpms.enums.OperationType;
import com.xiaolin.mpms.service.UserService;
import com.xiaolin.mpms.entity.VO.LoginVO;
import com.xiaolin.mpms.entity.VO.ResultVO;
import com.xiaolin.mpms.entity.user.User;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user")
@Api(tags = "用户")
public class UserController {


    @Autowired
    private UserService userService;

    /**
     * 判断用户是否存在
     *
     * @param user 登录用户数据
     * @return 用户是否存在
     */
    @PostMapping("exist")
    @ApiOperation(value = "判断用户是否存在", notes = "判断用户是否存在", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "数据获取成功")
    })
    public ResultVO<Boolean> isUserExist(@RequestBody LoginVO user) {
        Boolean result = userService.isUserExist(user.getUsername());
        return ResultVO.success("数据获取成功", result);
    }

    /**
     * 获取用户列表
     *
     * @param current 当前页
     * @param size    每页条数
     * @return 用户列表
     */
    @GetMapping("list/page")
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 200, message = "数据获取成功")
    })
    public ResultVO<Page<User>> getUSerListPage(@RequestParam(defaultValue = "1") int current, @RequestParam(defaultValue = "5") int size, @RequestParam(required = false) Map<String, String> filter) {
        Page<User> userList = userService.getUserListPage(current, size, filter);
        return ResultVO.success("数据获取成功", userList);
    }

    @GetMapping("list/select")
    @ApiOperation(value = "获取用户选择列表", notes = "获取用户列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 200, message = "数据获取成功")
    })
    public ResultVO<List<User>> getUserListSelect() {
        List<User> userList = userService.getUserListSelect();
        return ResultVO.success("数据获取成功", userList);
    }

    /**
     * 获取用户信息
     *
     * @return 获取带权用户信息
     */
    @GetMapping("")
    @ApiOperation(value = "获取带权用户信息", notes = "获取带权用户信息", httpMethod = "GET", produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 200, message = "数据获取成功")
    })
    public ResultVO<UserVO> getUserInfo() {
        UserVO user = userService.getUserInfoWithPermission();
        return ResultVO.success("数据获取成功", user);
    }

    /**
     * 获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("{id}")
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", httpMethod = "GET", produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 200, message = "数据获取成功")
    })
    public ResultVO<UserVO> getUserInfo(@PathVariable Integer id) {
        UserVO user = userService.getUserInfo(id);
        return ResultVO.success("数据获取成功", user);
    }

    /**
     * 保存用户信息
     *
     * @param user 用户信息
     * @return 用户是否添加成功
     */
    @PostMapping("")
    @ApiOperation(value = "保存用户信息", notes = "保存用户信息", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "数据保存成功")
    })
    public ResultVO<Boolean> saveUser(@RequestBody UserVO user) {
        return ResultVO.success("数据保存成功", userService.saveUser(user));
    }

    /**
     * 修改用户
     *
     * @param user 用户信息
     * @return 用户是否修改成功
     */
    @PutMapping("")
    @ApiOperation(value = "修改用户", notes = "修改用户", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "user", value = "用户信息", required = true, dataType = "User", paramType = "body")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "数据保存成功")
    })
    @Log(title = "修改用户", operationType = OperationType.UPDATE, logType = LogType.SYSTEM)
    public ResultVO<Boolean> updateUser(@RequestBody UserVO user) {
        return ResultVO.success("数据保存成功", userService.saveUser(user));
    }

    /**
     * 重置用户密码
     *
     * @param id 用户ID
     * @return 用户是否重置成功
     */
    @PutMapping("/password/reset")
    @ApiOperation(value = "重置用户密码", notes = "重置用户密码", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "int", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "数据保存成功")
    })
    public ResultVO<Boolean> resetPassword(Integer id, String initialPassword) {
        return ResultVO.success("数据保存成功", userService.resetPassword(id, initialPassword));
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 用户是否删除成功
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除用户", notes = "删除用户", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "int", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "数据删除成功")
    })
    public ResultVO<Boolean> deleteUser(@PathVariable int id) {
        return ResultVO.success("数据删除成功", userService.deleteUser(id));
    }

    /**
     * 批量删除用户
     *
     * @param ids 用户ID列表
     * @return 用户是否删除成功
     */
    @DeleteMapping("")
    @ApiOperation(value = "批量删除用户", notes = "批量删除用户", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "用户ID列表", required = true, dataType = "List", paramType = "body")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "数据删除成功")
    })
    public ResultVO<Boolean> deleteUser(@RequestBody List<Integer> ids) {
        return ResultVO.success("数据删除成功", userService.deleteUser(ids));
    }

    @PutMapping("profile")
    public ResultVO<Boolean> updateProfile(@RequestBody UserVO user) {
        return ResultVO.success("数据保存成功", userService.updateProfile(user));
    }

    @PutMapping("password/update")
    public ResultVO<Boolean> updateUserPassword(@RequestBody Map<String, String> data) {
        return ResultVO.success("数据保存成功", userService.updateUserPassword(data));
    }

    @PutMapping("profile/avatar")
    public ResultVO<Boolean> updateAvatar(@RequestBody UserVO user) {
        return ResultVO.success("数据保存成功", userService.updateAvatar(user));
    }
}
