package kr.xit.core.aop;

import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.security.web.firewall.RequestRejectedException;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * description : http://localhost:8080/api-docs/%27http://localhost:8080%27/api/auth/authenticate 형태의 request 에러 메세지 skip
 * packageName : kr.xit.core.aop
 * fileName    : FilterChainProxyAspect
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
//@Aspect
//@Component
@Slf4j
public class FilterChainProxyAspect {

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
