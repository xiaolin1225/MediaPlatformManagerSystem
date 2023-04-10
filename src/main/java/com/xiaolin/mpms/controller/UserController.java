/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.mpms.service.UserService;
import com.xiaolin.mpms.entity.LoginDto;
import com.xiaolin.mpms.entity.ResultVO;
import com.xiaolin.mpms.entity.User;
import com.xiaolin.mpms.utils.UUIDUtil;
import io.swagger.annotations.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user")
@Api(tags = "用户管理")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public ResultVO<Boolean> isUserExist(@RequestBody LoginDto user) {
        System.out.println(user);
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
    @GetMapping("list")
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 200, message = "数据获取成功")
    })
    public ResultVO<Page<User>> getUSerList(@RequestParam(defaultValue = "1") int current, @RequestParam(defaultValue = "5") int size, @RequestParam(required = false) Map<String, String> filter) {
        filter.remove("current");
        filter.remove("size");
        Page<User> userList = userService.getUserList(current, size, filter);
        return ResultVO.success("数据获取成功", userList);
    }


    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping()
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", httpMethod = "GET", produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 200, message = "数据获取成功")
    })
    public ResultVO<User> getUserInfo(@RequestParam(required = false) Integer id) {
        User user = userService.getUserInfo(id);
        return ResultVO.success("数据获取成功", user);
    }

    /**
     * 添加用户
     *
     * @param user 用户信息
     * @return 用户是否添加成功
     */
    @PostMapping("")
    @ApiOperation(value = "添加用户", notes = "添加用户", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "数据保存成功")
    })
    public ResultVO<Object> addUser(@RequestBody User user) {
        String uuid = UUIDUtil.getUUID();
        String encryptPassword = passwordEncoder.encode(user.getInitialPassword());
        user.setUid(uuid);
        user.setPassword(encryptPassword);
        user.setStatus(1);
        userService.addUser(user);
        System.out.println(user);
        return ResultVO.success("数据保存成功");
    }

    /**
     * 修改用户
     *
     * @param id   用户ID
     * @param user 用户信息
     * @return 用户是否修改成功
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "修改用户", notes = "修改用户", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "user", value = "用户信息", required = true, dataType = "User", paramType = "body")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "数据保存成功")
    })
    public ResultVO<Object> updateUser(@PathVariable int id, @RequestBody User user) {
        user.setId(id);
        userService.updateUser(user);
        return ResultVO.success("数据保存成功");
    }

    /**
     * 重置用户密码
     *
     * @param id 用户ID
     * @return 用户是否重置成功
     */
    @PutMapping("/{id}/password/reset")
    @ApiOperation(value = "重置用户密码", notes = "重置用户密码", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "int", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "数据保存成功")
    })
    public ResultVO<Object> resetPassword(@PathVariable int id, String initialPassword) {
        User userInfo = userService.getUserInfo(id);
        String encryptPassword = passwordEncoder.encode(initialPassword);
        userInfo.setPassword(encryptPassword);
        userService.updateUser(userInfo);
        return ResultVO.success("数据保存成功");
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
    public ResultVO<Object> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResultVO.success("数据删除成功");
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
    public ResultVO<Object> deleteUser(@RequestBody List<Integer> ids) {
        userService.deleteUser(ids);
        return ResultVO.success("数据删除成功");
    }
}
