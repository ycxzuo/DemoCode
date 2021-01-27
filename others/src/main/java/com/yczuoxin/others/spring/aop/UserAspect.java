package com.yczuoxin.others.spring.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class UserAspect implements Ordered {

    @Pointcut("execution(* com.yczuoxin.others.spring.service.UserService.*(..))")
    public void logAspect() {}

    @Before("logAspect()")
    public void doBefore(JoinPoint joinPoint) {
        System.out.println("do before");
        System.out.println("参数：" + Arrays.toString(joinPoint.getArgs()));
    }

    @After("logAspect()")
    public void doAfter(JoinPoint joinPoint) {
        System.out.println("do after");
        System.out.println("参数：" + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(value = "logAspect()", returning = "obj")
    public void doAfter(JoinPoint joinPoint, Object obj) {
        System.out.println("do AfterReturning");
        System.out.println(joinPoint);
        System.out.println("返回值：" + obj);
    }

    @Around("logAspect()")
    public void doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("do around before");
        System.out.println("参数：" + Arrays.toString(joinPoint.getArgs()));
        joinPoint.proceed();
        System.out.println("do around after");
        System.out.println(joinPoint);
    }

    @Override
    public int getOrder() {
        return 3;
    }
}
