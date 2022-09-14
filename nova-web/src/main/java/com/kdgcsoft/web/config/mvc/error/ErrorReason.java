package com.kdgcsoft.web.config.mvc.error;

import lombok.Getter;
import lombok.Setter;

/**
 * @author fyin
 * @date 2022年09月01日 10:34
 */
@Getter
@Setter
public class ErrorReason {
    private String className;
    private String description;
    private int httpCode;

    public ErrorReason(String className, String description, int httpCode) {
        this.className = className;
        this.description = description;
        this.httpCode = httpCode;
    }
}
