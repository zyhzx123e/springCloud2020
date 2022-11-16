package com.atguigu.springcloud.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.apache.commons.lang.StringUtils.substringAfterLast;

@Aspect
@Slf4j
@Configuration
@ConditionalOnExpression("${aspect.enabled:true}")
public class ExecutionTimeAdvice {

    @Around("com.atguigu.springcloud.aop.JoinPointConfig.servicePoint()")
    public Object servicePointAdvice(ProceedingJoinPoint point) throws Throwable {//service point cut for time tracking
        LocalDateTime startTimeLoc = LocalDateTime.now();
        String methodName = point.getSignature().getName();
        String className = substringAfterLast(point.getSignature().getDeclaringTypeName(), ".");
        log.info("servicePointAdvice Endpoint Invoked in {}.{}", className, methodName);
        Object object = point.proceed();
        log.info("servicePointAdvice Endpoint Completed in {}.{} Time_taken: {}ms", className, methodName,
                getExecutionTime(startTimeLoc, LocalDateTime.now()));
        return object;
    }

   long getExecutionTime(  LocalDateTime startTimeLoc,   LocalDateTime endTimeLoc){
        long l = Duration.between(startTimeLoc, endTimeLoc).toMillis();
        return l;
    }
}
