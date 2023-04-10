/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.xiaolin.mpms.mapper.FileTypeMapper;
import com.xiaolin.mpms.mapper.UserMapper;
import com.xiaolin.mpms.entity.FileType;
import com.xiaolin.mpms.entity.ResultVO;
import com.xiaolin.mpms.entity.User;
import com.xiaolin.mpms.utils.UUIDUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("utils")
public class TestController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FileTypeMapper fileTypeMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("uuid")
    public Map<String, Object> generateUUIDAndPassword(String password) {
        String uuid = UUIDUtil.getUUID();
        String salt = RandomStringUtils.randomAlphanumeric(6);
        String encryptPassword = DigestUtils.md5Hex(salt + password);
        Map<String, Object> data = new HashMap<>();
        data.put("uuid", uuid);
        data.put("salt", salt);
        data.put("encryptPassword", encryptPassword);
        return data;
    }

    @GetMapping("users")
    public List<User> generateUsers(int count) {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setUid(UUIDUtil.getUUID());
            user.setUsername("user" + i);
            user.setNickname("用户" + i);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setAvatar("http://localhost:8083/directlink/localhost/avatar.jpg");
            user.setEmail("user" + i + "@xiaolin.com");
            user.setPhone(RandomStringUtils.randomNumeric(11));
            userMapper.insert(user);
            list.add(user);
        }
        return list;
    }

    @PostMapping("file-type")
    public ResultVO<String> generateFileType(@RequestParam(defaultValue = "0") int folderId, @RequestBody(required = false) List<String> types) {
        System.out.println(folderId);
        if (types != null) {
            int count = 0;
            for (String type : types) {
                FileType fileType = new FileType(folderId,type,type);
                count += fileTypeMapper.insert(fileType);
            }
            return ResultVO.success("数据保存成功",String.format("成功插入%d条数据，共%d条数据", count, types.size()));
        }
        return ResultVO.error(70001, "数据保存失败,数据为空");
    }
}
