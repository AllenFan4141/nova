package com.kdgcsoft.common.exception;

/**
 * @author fyin
 * @date 2022年08月30日 14:28
 */
public class BizException extends RuntimeException{
    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }
}
