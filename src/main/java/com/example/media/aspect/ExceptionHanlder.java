package com.example.media.aspect;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHanlder {
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler({ Exception.class })
    @ResponseBody
    public Map<String, Object> handleError(Exception e, HandlerMethod handlerMethod) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("stacktrace", ExceptionUtils.getStackTrace(e));
        logger.error(ExceptionUtils.getStackTrace(e));
        return errorMap;
    }
}
