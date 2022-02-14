package com.atguigu.springcloud.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//-2147483648(INT.MIN_VALUE) ~ 2147483647(INT.MAX_VALUE)
//this order is important bcz in spring config it run the for loop will use this exceptionResolver 1st and return back,
//so that other ExceptionResolver like [DefaultErrorAttributes] & [HandlerExceptionResolverComposite] would not be executed
@Order(value = Ordered.HIGHEST_PRECEDENCE)//lower the number has higher priority
@Component
public class CustomHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        try {
            httpServletResponse.sendError(511,"My Own Error");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new ModelAndView();
    }
}
