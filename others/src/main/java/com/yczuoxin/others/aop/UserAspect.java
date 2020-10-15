package com.yczuoxin.others.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserAspect implements Ordered {

    @Pointcut("execution(* com.yczuoxin.others.service.UserService.*(..))")
    public void logAspect() {}

    @Before("logAspect()")
    public void doBefore(JoinPoint joinPoint) {
        System.out.println("do before");
        System.out.println(joinPoint);
    }

    @After("logAspect()")
    public void doAfter(JoinPoint joinPoint) {
        System.out.println("do after");
        System.out.println(joinPoint);
    }

    @AfterReturning(value = "logAspect()", returning = "obj")
    public void doAfter(JoinPoint joinPoint, Object obj) {
        System.out.println("do AfterReturning");
        System.out.println(joinPoint);
        System.out.println(obj);
    }

    @Around("logAspect()")
    public void doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("do around");
        joinPoint.proceed();
        System.out.println(joinPoint);
    }

    @Override
    public int getOrder() {
        return 3;
    }
}
