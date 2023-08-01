/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.exception;

public class FileException extends BaseException{
    public FileException() {
    }

    public FileException(String message) {
        super(message);
    }

    public FileException(Integer code, String message) {
        super(code, message);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public FileException(Throwable cause) {
        super(cause);
    }

    public FileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public FileException(Integer code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
