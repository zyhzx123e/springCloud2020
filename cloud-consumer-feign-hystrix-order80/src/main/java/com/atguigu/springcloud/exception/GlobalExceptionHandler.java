package com.atguigu.springcloud.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ArithmeticException.class,
            MissingServletRequestParameterException.class,
            NullPointerException.class})
    public String handleArithmeticException(Exception ex){
        //this method can return String as viewname(can be found in template static path {viewname}.html)
        //or can return ModelAndView(include both view and model)

        log.error("catched exception:{}",ex);
        return "login";
    }

}
