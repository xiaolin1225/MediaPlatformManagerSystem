/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.exception;

public class ContentException extends BaseException{
    public ContentException() {
    }

    public ContentException(String message) {
        super(message);
    }

    public ContentException(Integer code, String message) {
        super(code, message);
    }

    public ContentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContentException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ContentException(Throwable cause) {
        super(cause);
    }

    public ContentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ContentException(Integer code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
