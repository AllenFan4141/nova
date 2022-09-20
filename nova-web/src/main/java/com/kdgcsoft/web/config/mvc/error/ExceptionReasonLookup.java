package com.kdgcsoft.web.config.mvc.error;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpStatus;
import com.kdgcsoft.common.exception.BizException;
import com.kdgcsoft.web.common.model.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fyin
 * @date 2021-02-26 10:21
 * 用于将一些能明确下来的异常转化成更容易理解的中文描述,可包含解决方法等描述信息,方便快速排查
 */
@Slf4j
public class ExceptionReasonLookup {
    private static final Map<String, ErrorReason> REASON_MAP = new HashMap<>();
    private static final List<ErrorReason> REASON_LIST = new ArrayList<>();

    static {
        REASON_LIST.add(new ErrorReason("NullPointerException", "空指针调用", HttpStatus.HTTP_INTERNAL_ERROR));
        REASON_LIST.add(new ErrorReason("NoHandlerFoundException", "请求资源未找到", HttpStatus.HTTP_NOT_FOUND));
        REASON_LIST.add(new ErrorReason("MethodArgumentTypeMismatchException", "参数类型不匹配", HttpStatus.HTTP_BAD_REQUEST));
        REASON_LIST.add(new ErrorReason("HttpRequestMethodNotSupportedException", "不支持的请求方式", HttpStatus.HTTP_BAD_METHOD));
        REASON_LIST.add(new ErrorReason("TransactionSystemException", "数据提交事务异常", HttpStatus.HTTP_INTERNAL_ERROR));
        REASON_LIST.add(new ErrorReason("BindException", "后台参数绑定异常", HttpStatus.HTTP_BAD_REQUEST));
        REASON_LIST.add(new ErrorReason("TransactionRequiredException", "操作需要注入事务@Transactional", HttpStatus.HTTP_INTERNAL_ERROR));
        REASON_LIST.add(new ErrorReason("SQLGrammarException", "SQL语法出错", HttpStatus.HTTP_INTERNAL_ERROR));
        REASON_LIST.add(new ErrorReason("SQLSyntaxErrorException", "SQL语法出错", HttpStatus.HTTP_INTERNAL_ERROR));
        REASON_LIST.add(new ErrorReason("InvalidDataAccessApiUsageException", "数据访问API调用出错", HttpStatus.HTTP_INTERNAL_ERROR));
        REASON_LIST.add(new ErrorReason("IllegalArgumentException", "非法参数调用", HttpStatus.HTTP_INTERNAL_ERROR));
        REASON_LIST.add(new ErrorReason("ValidationException", "参数校验出错", HttpStatus.HTTP_BAD_REQUEST));
        REASON_LIST.add(new ErrorReason("ConstraintViolationException", "参数校验出错", HttpStatus.HTTP_BAD_REQUEST));
        REASON_LIST.add(new ErrorReason("MethodArgumentNotValidException", "参数校验出错", HttpStatus.HTTP_BAD_REQUEST));

        REASON_LIST.add(new ErrorReason("QueryException", "查询调用出错", HttpStatus.HTTP_INTERNAL_ERROR));
        for (ErrorReason reason : REASON_LIST) {
            REASON_MAP.put(reason.getClassName(), reason);
        }
    }

    public static JsonResult getReason(Throwable e) {
        log.error(e.getMessage(), e);
        if (e != null) {
            if (e instanceof BizException) {
                return JsonResult.ERROR(e.getMessage()).code(HttpStatus.HTTP_INTERNAL_ERROR);
            }
//            if (e instanceof BindException) {
//                BindException t = (BindException) e;
//                List<String> messages = new ArrayList<>();
//                t.getAllErrors().forEach(objectError -> {
//                    Class declaringClass = objectError.getClass().getDeclaringClass();
//                    if (declaringClass == SpringValidatorAdapter.class) {
//                        messages.add(objectError.getDefaultMessage());
//                    } else if (declaringClass == null) {
//                        FieldError fieldError = (FieldError) objectError;
//                        messages.add("参数绑定出错-" + fieldError.getObjectName() + "." + fieldError.getField() + ":" + fieldError.getRejectedValue());
//                    } else {
//                        FieldError fieldError = (FieldError) objectError;
//                        messages.add("参数绑定出错-" + fieldError.getObjectName() + "." + fieldError.getField() + ":" + fieldError.getRejectedValue());
//                    }
//                });
//                String message = CollUtil.join(messages, ",");
//                return JsonResult.ERROR(message).code(HttpStatus.HTTP_BAD_REQUEST);
//            }
//
//            if (e instanceof ConstraintViolationException) {
//                List<String> messages = new ArrayList<>();
//                ConstraintViolationException t = (ConstraintViolationException) e;
//                t.getConstraintViolations().forEach(constraintViolation -> {
//                    PathImpl pathImpl = (PathImpl) constraintViolation.getPropertyPath();
//                    // 读取参数字段，constraintViolation.getMessage() 读取验证注解中的message值
////                    String paramName = pathImpl.getLeafNode().getName();
////                    String message = "参数{".concat(paramName).concat("}").concat(constraintViolation.getMessage());
//                    String message = constraintViolation.getMessage();
//                    messages.add(message);
//                });
//                String message = CollUtil.join(messages, ",");
//                return JsonResult.ERROR(message).code(HttpStatus.HTTP_BAD_REQUEST);
//            }

            String exceptionName = e.getClass().getSimpleName();
            if (REASON_MAP.containsKey(exceptionName)) {
                ErrorReason reason = REASON_MAP.get(exceptionName);
                return JsonResult.ERROR(reason.getDescription()).code(reason.getHttpCode());
            } else {
                return JsonResult.ERROR("应用内部错误").code(HttpStatus.HTTP_INTERNAL_ERROR);
            }
        } else {
            return JsonResult.ERROR().code(HttpStatus.HTTP_INTERNAL_ERROR);
        }
    }

    public static void addReason(ErrorReason reason) {
        if (reason != null) {
            REASON_LIST.add(reason);
            REASON_MAP.put(reason.getClassName(), reason);
        }
    }
}
