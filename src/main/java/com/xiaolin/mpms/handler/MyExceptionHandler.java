/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.handler;


import com.xiaolin.mpms.entity.VO.ResultVO;
import com.xiaolin.mpms.exception.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLSyntaxErrorException;
import java.util.Objects;

@Log4j2
@RestControllerAdvice
public class MyExceptionHandler {


    @ExceptionHandler(FileException.class)
    public ResultVO<Throwable> handlerFileException(FileException e) {
        String message = "文件操作执行异常";
        message = e.getMessage() != null && !Objects.equals(e.getMessage(), "null") ? e.getMessage() : message;
        log.error(message, e);
        return ResultVO.error(e.getCode() != null ? e.getCode() : 3000, message, e.getCause());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResultVO<Throwable> handleUserException(AuthenticationException e) {
        String message = "未登录，请先登录！";
        message = e.getMessage() != null && !Objects.equals(e.getMessage(), "null") ? e.getMessage() : message;
        log.error(message, e);
        return ResultVO.error(2001, message, e.getCause());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResultVO<Throwable> handleUserException(AccessDeniedException e) {
        String message = "暂无权限，请联系管理员！";
        message = e.getMessage() != null && !Objects.equals(e.getMessage(), "null") ? e.getMessage() : message;
        log.error(message, e);
        return ResultVO.error(2003, message, e.getCause());
    }

    @ExceptionHandler(UserError.class)
    public ResultVO<Throwable> handleUserException(UserError e) {
        String message = "用户数据操作执行异常";
        message = e.getMessage() != null && !Objects.equals(e.getMessage(), "null") ? e.getMessage() : message;
        log.error(message, e);
        return ResultVO.error(e.getCode() != null ? e.getCode() : 4000, message, e.getCause());
    }

    @ExceptionHandler(RoleException.class)
    public ResultVO<Throwable> handleRoleException(RoleException e) {
        String message = "角色数据操作执行异常";
        message = e.getMessage() != null && !Objects.equals(e.getMessage(), "null") ? e.getMessage() : message;
        return ResultVO.error(e.getCode() != null ? e.getCode() : 4010, message, e.getCause());
    }

    @ExceptionHandler(DepartmentException.class)
    public ResultVO<Throwable> handleDepartmentException(DepartmentException e) {
        String message = "部门数据操作执行异常";
        message = e.getMessage() != null && !Objects.equals(e.getMessage(), "null") ? e.getMessage() : message;
        log.error(message, e);
        return ResultVO.error(e.getCode() != null ? e.getCode() : 4020, message, e.getCause());
    }

    @ExceptionHandler(PositionException.class)
    public ResultVO<Throwable> handlePositionException(PositionException e) {
        String message = "职位数据操作执行异常";
        message = e.getMessage() != null && !Objects.equals(e.getMessage(), "null") ? e.getMessage() : message;
        log.error(message, e);
        return ResultVO.error(e.getCode() != null ? e.getCode() : 4030, message, e.getCause());
    }

    @ExceptionHandler(ContentException.class)
    public ResultVO<Throwable> handleContentException(ContentException e) {
        String message = "内容数据操作执行异常";
        message = e.getMessage() != null && !Objects.equals(e.getMessage(), "null") ? e.getMessage() : message;
        log.error(message, e);
        return ResultVO.error(e.getCode() != null ? e.getCode() : 6000, message, e.getCause());
    }

    @ExceptionHandler(PlatformException.class)
    public ResultVO<Throwable> handlePlatformException(PlatformException e) {
        String message = "平台操作执行异常";
        message = e.getMessage() != null && !Objects.equals(e.getMessage(), "null") ? e.getMessage() : message;
        log.error(message, e);
        return ResultVO.error(e.getCode() != null ? e.getCode() : 7000, message, e.getCause());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResultVO<Throwable> handlerRuntimeException(RuntimeException e) {
        String message = "服务器运行异常";
        message = e.getMessage() != null && !Objects.equals(e.getMessage(), "null") ? e.getMessage() : message;
        log.error(message, e);
        return ResultVO.error(5000, message, e.getCause());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResultVO<Throwable> handlerNullPointerException(NullPointerException e) {
        String message = "参数不正确，请检查后提交";
        message = e.getMessage() != null && !Objects.equals(e.getMessage(), "null") ? e.getMessage() : message;
        log.error(message, e);
        return ResultVO.error(5001, message, e.getCause());
    }

    @ExceptionHandler(SQLSyntaxErrorException.class)
    public ResultVO<Throwable> handlerSQLException(SQLSyntaxErrorException e) {
        String message = "数据库查询失败";
        message = e.getMessage() != null && !Objects.equals(e.getMessage(), "null") ? e.getMessage() : message;
        log.error(message, e);
        return ResultVO.error(5002, message, e.getCause());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResultVO<Throwable> handlerDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message = "数据库运行异常";
        message = e.getMessage() != null && !Objects.equals(e.getMessage(), "null") ? e.getMessage() : message;
        log.error(message, e);
        return ResultVO.error(5003, message, e.getCause());
    }

    @ExceptionHandler(IllegalRequestException.class)
    public ResultVO<Throwable> handlerIllegalRequestException(IllegalRequestException e) {
        String message = "非法请求";
        message = e.getMessage() != null && !Objects.equals(e.getMessage(), "null") ? e.getMessage() : message;
        log.error(message, e);
        return ResultVO.error(5004, message, e.getCause());
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResultVO<Throwable> handlerParseException(RuntimeException e) {
        String message = "参数未传递或参数类型错误";
        message = e.getMessage() != null && !Objects.equals(e.getMessage(), "null") ? e.getMessage() : message;
        log.error(message, e);
        return ResultVO.error(5000, message, e.getCause());
    }
}
