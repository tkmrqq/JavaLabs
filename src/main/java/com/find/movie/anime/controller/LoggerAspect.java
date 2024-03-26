package com.find.movie.anime.controller;

import com.find.movie.anime.AnimeService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class LoggerAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnimeService.class);

    @Before("execution(* com.find.movie.anime.controller..*(..))")
    public void logMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        LOGGER.info("Calling method: " + methodName);
    }

    @AfterThrowing(pointcut = "execution(* com.find.movie.anime.controller..*(..))", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().toShortString();
        LOGGER.error("Exception thrown in method: " + methodName, ex);
    }

}
