/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.exception;

public class PositionException extends BaseException{
    public PositionException() {
    }

    public PositionException(String message) {
        super(message);
    }

    public PositionException(Integer code, String message) {
        super(code, message);
    }

    public PositionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PositionException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public PositionException(Throwable cause) {
        super(cause);
    }

    public PositionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public PositionException(Integer code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
