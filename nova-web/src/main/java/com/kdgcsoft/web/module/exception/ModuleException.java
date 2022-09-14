package com.kdgcsoft.web.module.exception;

import com.kdgcsoft.common.exception.BizException;

/**
 * @author fyin
 * @date 2022年09月01日 17:32
 */
public class ModuleException extends BizException {

    public ModuleException(String message, Throwable cause) {
        super(message, cause);
    }


    public ModuleException(String message) {
        super(message);
    }
}
