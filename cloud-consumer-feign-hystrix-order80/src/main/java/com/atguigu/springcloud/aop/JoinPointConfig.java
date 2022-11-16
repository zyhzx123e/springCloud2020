package com.atguigu.springcloud.aop;

import org.aspectj.lang.annotation.Pointcut;
public class JoinPointConfig {

    @Pointcut("execution(* com.atguigu.springcloud.service.*.*(..))")
    public void servicePoint(){}

}
