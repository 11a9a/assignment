package com.example.assingment.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class TransactionServiceAOP {

    private static void logRequest(JoinPoint joinPoint) {
        log.info("Method Location {}", joinPoint.getSignature());
        log.info("Request Fields {}", Arrays.toString(joinPoint.getArgs()));
    }

    @Before(value = "execution(* com.example.assingment.service.TransactionService.topUp(..))")
    public void beforePay(JoinPoint joinPoint) {
        log.info("Url: api/v1/transactions/{customerId}/top-up");
        logRequest(joinPoint);
    }

    @Before(value = "execution(* com.example.assingment.service.TransactionService.purchase(..))")
    public void beforeConfirmOrder(JoinPoint joinPoint) {
        log.info("Url: api/v1/transactions/{customerId}/purchase");
        logRequest(joinPoint);
    }

    @Before(value = "execution(* com.example.assingment.service.TransactionService.refund(..))")
    public void beforeVoidTransaction(JoinPoint joinPoint) {
        log.info("Url: api/v1/transactions/{customerId}/refund");
        logRequest(joinPoint);
    }

    @AfterReturning(value = "execution(* com.example.assingment.service.TransactionService.*(..))", returning = "result")
    public void afterAllMethodsInAllServices(Object result) {
        log.info("Response is {}", result);
    }
}
