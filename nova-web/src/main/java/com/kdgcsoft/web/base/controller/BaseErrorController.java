package com.kdgcsoft.web.base.controller;

import com.kdgcsoft.web.common.model.JsonResult;
import com.kdgcsoft.web.config.mvc.error.ErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author fyin
 * @date 2022年08月26日 10:01
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
@Slf4j
public class BaseErrorController extends AbstractErrorController {
    public static String ERROR_PAGE = "error.html";
    private final ErrorAttributes errorAttributes;


    public BaseErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
        this.errorAttributes = errorAttributes;
    }

    /**
     * 情况1：若预期返回类型为text/html,则返回错误信息页(View).
     */
    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView view = new ModelAndView(ERROR_PAGE);
        JsonResult result = buildJsonResult(request);
        view.addObject("errorInfo", result);
        return view;
    }

    /**
     * 情况2：其它预期类型 则返回详细的错误信息(JSON).
     */
    @RequestMapping
    @ResponseBody
    public JsonResult error(HttpServletRequest request, HttpServletResponse response) {
        JsonResult result = buildJsonResult(request);
        return result;
    }

    private JsonResult buildJsonResult(HttpServletRequest request) {
        Throwable t = this.errorAttributes.getError(new ServletWebRequest(request));
        ErrorInfo errorInfo = ErrorInfo.build(request, t);
        return JsonResult.ERROR(errorInfo.getMessage()).data(errorInfo).code(errorInfo.getCode());
    }

}
