package com.kdgcsoft.web.config.mvc.error;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kdgcsoft.web.common.model.JsonResult;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.WebUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author fyin
 * @date 2021-04-26 14:23
 */
@Data
@ApiModel(value = "异常信息")
public class ErrorInfo implements Serializable {
    /**
     * 访问Url
     */
    private String url;
    /**
     * 发生时间
     */
    private String time;
    /**
     * 错误信息
     */
    private String message;
    /**
     * 错误http编码
     */
    private int code;
    /**
     * 错误的堆栈轨迹
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String stackTrace;

    public static ErrorInfo build(HttpServletRequest request, Exception e) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setTime(LocalDateTime.now().toString());
        errorInfo.setUrl(request.getRequestURI());
        Throwable root = ExceptionUtil.getRootCause(e);
        errorInfo.setMessage(root.getClass().getName() + ":" + e.getMessage());
        return errorInfo;
    }

    public static ErrorInfo build(HttpServletRequest request, Throwable error) {
        HttpStatus httpStatus = getStatus(request);
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setTime(LocalDateTime.now().toString());
        errorInfo.setUrl((String) request.getAttribute(WebUtils.ERROR_REQUEST_URI_ATTRIBUTE));
        errorInfo.setCode(httpStatus.value());
        if (error != null) {
            Throwable root = ExceptionUtil.getRootCause(error);
            JsonResult errorResult = ExceptionReasonLookup.getReason(root);
            errorInfo.setMessage(errorResult.getMsg());
            errorInfo.setCode(errorResult.getCode());
            errorInfo.setStackTrace(ExceptionUtil.stacktraceToString(error));
        } else {
            errorInfo.setMessage(httpStatus.getReasonPhrase());
        }
        return errorInfo;
    }

    public static HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
