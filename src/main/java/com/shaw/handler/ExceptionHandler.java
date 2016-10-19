package com.shaw.handler;

import com.shaw.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shaw on 2016/10/19 0019.
 */
public class ExceptionHandler implements HandlerExceptionResolver {
    Logger logger = LoggerFactory.getLogger(Constants.LOG_EXCEPTION);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        logger.error("Blog System Exception：", ex);
        return new ModelAndView("WEB-INF/error");
    }
}
