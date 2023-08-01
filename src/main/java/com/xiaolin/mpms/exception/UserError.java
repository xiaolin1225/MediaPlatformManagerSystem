/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.exception;

public class UserError extends BaseException{
    public UserError() {
    }

    public UserError(String message) {
        super(message);
    }

    public UserError(Integer code, String message) {
        super(code, message);
    }

    public UserError(String message, Throwable cause) {
        super(message, cause);
    }

    public UserError(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public UserError(Throwable cause) {
        super(cause);
    }

    public UserError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UserError(Integer code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
