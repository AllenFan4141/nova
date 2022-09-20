package com.kdgcsoft.document.exception;

import com.kdgcsoft.common.exception.BizException;

/**
 * @author fyin
 * @date 2021-05-19 14:48
 */
public class DocumentException extends BizException {

    public DocumentException(String message) {
        super(message);
    }

    public DocumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentException(Throwable cause) {
        super(cause.getMessage(),cause);
    }
}
