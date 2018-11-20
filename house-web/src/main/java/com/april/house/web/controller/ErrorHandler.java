package com.april.house.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @Creation :  2018-11-20 15:06
 * --------  ---------  --------------------------
 */
@ControllerAdvice
public class ErrorHandler {
    private static final Logger logger = LogManager.getLogger(ErrorHandler.class);

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public String error5xx(HttpServletRequest request, Exception e) {
        logger.error(e.getMessage(), e);
        logger.error(request.getRequestURL() + "encounter 5xx");
        return "error/5xx";
    }
}
