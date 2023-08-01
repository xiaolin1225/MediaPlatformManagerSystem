/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.exception;

public class RoleException extends BaseException{
    public RoleException() {
    }

    public RoleException(String message) {
        super(message);
    }

    public RoleException(Integer code, String message) {
        super(code, message);
    }

    public RoleException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoleException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public RoleException(Throwable cause) {
        super(cause);
    }

    public RoleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RoleException(Integer code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
