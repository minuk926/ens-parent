package kr.xit.core.spring.aop;

import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.security.web.firewall.RequestRejectedException;

import lombok.extern.slf4j.Slf4j;

/**
 * http://localhost:8080/api-docs/%27http://localhost:8080%27/api/auth/authenticate 형태의 request 에러 메세지 skip
 * @author Lim, Jong Uk (minuk926)
 * @since 2021-07-20
 */
//@Aspect
//@Component
@Slf4j
public class FilterChainProxyAdvice {

    @Around("execution(public void org.springframework.security.web.FilterChainProxy.doFilter(..))")
    public void handleRequestRejectedException (ProceedingJoinPoint pjp) throws Throwable {
        try {
            pjp.proceed();
        } catch (RequestRejectedException exception) {
            log.error("===== Request url error::{} =====", exception.getLocalizedMessage());
            HttpServletResponse response = (HttpServletResponse) pjp.getArgs()[1];
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
