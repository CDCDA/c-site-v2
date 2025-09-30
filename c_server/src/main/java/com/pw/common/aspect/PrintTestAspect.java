package com.pw.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/***
 * @author cyd
 * @date 2023/7/31 10:04
 * @description <测试切面----打印>
 * 升级至 Spring Boot 3 格式，引入 Slf4j 日志
 **/
@Slf4j
@Aspect
@Component
public class PrintTestAspect {

    /**
     * 定义一个切入点，匹配 com.pw.service 包下所有类的所有方法
     */
    @Pointcut("execution(* com.pw.service.*.*(..))")
    public void serviceMethods() {
    }

    /**
     * 环绕通知：计算方法执行时间。
     */
    @Around("serviceMethods()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getSignature().getName();
        String className = pjp.getTarget().getClass().getSimpleName();
        Long start = System.currentTimeMillis();

        try {
            log.debug("[{}.{}] 方法开始执行...", className, methodName);
            Object result = pjp.proceed();
            Long end = System.currentTimeMillis();
            log.debug("[{}.{}] 方法执行成功，耗时: {} ms", className, methodName, (end - start));
            return result;
        } catch (Throwable e) {
            Long end = System.currentTimeMillis();
            log.error("[{}.{}] 方法执行失败，耗时: {} ms，异常信息: {}", className, methodName, (end - start), e.getMessage(), e); // 【优化】记录异常堆栈
            throw e;
        }
    }

    /**
     * 返回通知：在方法成功返回后执行。
     */
    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void afterReturning(JoinPoint jp, Object result) {
        if (log.isDebugEnabled()) { // 【优化】debug级别下才记录返回值，避免生产环境日志过多
            String methodName = jp.getSignature().getName();
            String className = jp.getTarget().getClass().getSimpleName();
            log.debug("[{}.{}] 方法返回值为: {}", className, methodName, result);
        }
    }

    /**
     * 异常通知：在方法抛出异常后执行。
     */
//    @AfterThrowing(pointcut = "serviceMethods()", throwing = "e")
//    public void afterThrowing(JoinPoint jp, Exception e) {
//    }

    /*
    @Before("serviceMethods()")
    public void before(JoinPoint jp) {
    }

    @After("serviceMethods()")
    public void after(JoinPoint jp) {
    }
    */
}
