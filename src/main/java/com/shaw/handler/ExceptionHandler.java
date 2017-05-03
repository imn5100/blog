package com.shaw.handler;

import com.shaw.constants.Constants;
import com.shaw.util.EmailUtils;
import com.shaw.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常捕获，写入日志
 */
public class ExceptionHandler implements HandlerExceptionResolver {
    Logger logger = LoggerFactory.getLogger(Constants.LOG_EXCEPTION);
    public static final String EMAIL_SUBSCRIBER = PropertiesUtil.getConfiguration().getString("email.subscriber");


    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        logger.error("Blog System Exception：", ex);
        EmailUtils.sendEmail("Blog system exception", org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace(ex), EMAIL_SUBSCRIBER);
        return new ModelAndView("WEB-INF/error");
    }
}
