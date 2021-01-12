package com.yczuoxin.others.spring.aop;

import com.yczuoxin.others.spring.annotation.TimeLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeLogAspect implements Ordered {

    @Around("@annotation(timeLog)")
    public Object printLog(ProceedingJoinPoint joinPoint, TimeLog timeLog) throws Throwable {
        System.out.println("进入 TimeLogAspect");
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        System.out.println(joinPoint.getTarget().getClass().getName()
                + "的"
                + joinPoint.getSignature().getName()
                + "方法耗时：" + (System.currentTimeMillis() - start));
        return proceed;
    }

    @Override
    public int getOrder() {
        return 4;
    }
}
