package com.kdgcsoft.web.base.aspect;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.kdgcsoft.web.base.anno.OptLog;
import com.kdgcsoft.web.base.entity.BaseOptLog;
import com.kdgcsoft.web.base.enums.Enabled;
import com.kdgcsoft.web.base.event.OptLogEvent;
import com.kdgcsoft.web.common.consts.WebConst;
import com.kdgcsoft.web.common.model.LoginUser;
import com.kdgcsoft.web.common.util.ServletUtils;
import com.kdgcsoft.web.config.security.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作日志的切面处理
 *
 * @author fyin
 * @date 2022年09月13日 16:02
 */
@Aspect
@Component
@Slf4j
public class OptLogAspect {
    @Autowired
    ApplicationEventPublisher publisher;
    /**
     * spring内部的方法参数名的转换器
     */
    private static final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    /**
     * SPEL表达式解析器
     */
    ExpressionParser parser = new SpelExpressionParser();
    StandardEvaluationContext parserContext;

    /**
     * 注册切入点
     */
    @Pointcut("@annotation(com.kdgcsoft.web.base.anno.OptLog)")
    public void optLogPointCut() {

    }

    @Around("optLogPointCut()")
    public Object aroundOptLog(ProceedingJoinPoint joinPoint) {
        parserContext = new StandardEvaluationContext();
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            handlerPointCut(joinPoint, startTime, result, null);
            return result;
        } catch (Throwable throwable) {
            handlerPointCut(joinPoint, startTime, null, throwable);
            throw new RuntimeException(throwable);
        }
    }

    private void handlerPointCut(ProceedingJoinPoint joinPoint, long startTime, Object result, Throwable throwable) {
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = methodSignature.getMethod();
            OptLog logAnno = method.getAnnotation(OptLog.class);
            if (logAnno == null) {
                return;
            }
            Map<String, Object> paramsMap = buildParamsMap(method, joinPoint);
            parserContext.setVariables(paramsMap);
            parserContext.setVariable(WebConst.LOGIN_USER, SecurityUtil.getLoginUser());
            BaseOptLog optLog = buildOptLogByAnno(logAnno, method);
            optLog.setTimeCost(System.currentTimeMillis() - startTime);
            fillLogExtInfo(optLog, logAnno, result, throwable, paramsMap);
            publisher.publishEvent(new OptLogEvent(optLog));
        } catch (Exception e) {
            log.error("操作日志记录出错", e);
        }

    }

    /**
     * 根据注解构建一些操作日志的基本信息
     *
     * @param logAnno
     * @param method
     * @return
     */
    public BaseOptLog buildOptLogByAnno(OptLog logAnno, Method method) {
        BaseOptLog optLog = new BaseOptLog();
        if (logAnno != null) {
            optLog.setLogTitle(logAnno.title());
            optLog.setOptType(logAnno.type());
            if (StrUtil.isNotEmpty(logAnno.operator())) {
                optLog.setOperatorName(parser.parseExpression(logAnno.operator()).getValue(parserContext, String.class));
            } else {
                LoginUser loginUser = SecurityUtil.getLoginUser();
                optLog.setOperatorId(loginUser == null ? 0 : loginUser.getUserId());
                optLog.setOperatorName(loginUser == null ? "无" : loginUser.getUsername());
            }
            optLog.setLogUrl(ServletUtils.getRequest().getRequestURI());
            optLog.setOptIp(ServletUtils.getClientIp());
            optLog.setHttpMethod(ServletUtils.getRequest().getMethod());
            optLog.setJavaMethod(method.getDeclaringClass().getName() + StrUtil.DOT + method.getName());
            optLog.setOptTime(new Date());

        }
        return optLog;
    }


    public Map<String, Object> buildParamsMap(Method method, ProceedingJoinPoint joinPoint) {
        String[] names = parameterNameDiscoverer.getParameterNames(method);
        Map<String, Object> paramsMap = MapUtil.newHashMap();
        if (ArrayUtil.isNotEmpty(names)) {
            for (int i = 0; i < names.length; i++) {
                Object arg = joinPoint.getArgs()[i];
                paramsMap.put(names[i], arg);
            }
        }
        return paramsMap;
    }

    /**
     * 填充一些更丰富的日志信息
     */
    public void fillLogExtInfo(BaseOptLog optLog, OptLog logAnno, Object result, Throwable throwable, Map<String, Object> paramsMap) {
        optLog.setSuccess(throwable == null ? Enabled.Y : Enabled.N);
        /**记录请求参数,如果是文件或者spring内置的一些类型则不记录**/
        if (logAnno.logRequest()) {
            Map<String, Object> logParams = new HashMap<>();
            for (String key : paramsMap.keySet()) {
                Object value = paramsMap.get(key);
                if (value instanceof HttpServletRequest || value instanceof HttpServletResponse || value instanceof Model || value instanceof LoginUser) {
                    //不记录Spring内部的一些对象和上传文件
                } else if (value instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) value;
                    Map<String, Object> fileInfo = new HashMap<>();
                    fileInfo.put("fileName", file.getName());
                    fileInfo.put("fileSize", file.getSize());
                    logParams.put(key, fileInfo);
                } else {
                    logParams.put(key, value);
                }
            }
            optLog.setRequest(JSON.toJSONString(logParams));

        }
        /**记录响应结果,如果出现异常则记录异常信息**/
        if (throwable != null) {
            optLog.setResponse(ExceptionUtil.stacktraceToString(throwable, 2000));
            if (StrUtil.isNotBlank(logAnno.error())) {
                parserContext.setVariable("_errorMsg", ExceptionUtil.getRootCause(throwable).getMessage());
                optLog.setOptDescription(parser.parseExpression(logAnno.success()).getValue(parserContext, String.class));
            }
        } else if (logAnno.logResponse() && result != null) {
            if (result instanceof ModelAndView) {
                optLog.setResponse("ModelAndView(" + ((ModelAndView) result).getViewName() + ")");
            } else if (result instanceof Throwable) {
                optLog.setResponse(ExceptionUtil.stacktraceToString((Throwable) result, 2000));
            } else {
                optLog.setResponse(StrUtil.maxLength(JSON.toJSONString(result), 2000));
            }
        }
    }
}
