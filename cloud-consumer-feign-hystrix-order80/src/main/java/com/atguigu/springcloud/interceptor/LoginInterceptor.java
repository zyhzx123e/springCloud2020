package com.atguigu.springcloud.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


//@Component //if added @Component, it add autowired into registry in WebMvcConfig
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    /*
    b4 execute handler
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        //return HandlerInterceptor.super.preHandle(request, response, handler);

        String requestURI = request.getRequestURI();
        log.info("intercepting requestURI{}",requestURI);
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");

        if(user!=null || 1==1)return true;

        session.setAttribute("msg","please login first");
        response.sendRedirect("/login");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
