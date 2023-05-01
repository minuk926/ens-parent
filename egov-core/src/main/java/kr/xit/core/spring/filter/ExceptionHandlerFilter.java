package kr.xit.core.spring.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import kr.xit.core.consts.ErrorCode;
import kr.xit.core.api.RestApiErrorResponse;
import kr.xit.core.exception.BizRuntimeException;
import kr.xit.core.support.utils.Checks;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * description : CustomExceptionHandler 와 함께 에러 처리
 *               - Filter에서 발생한 오류 처리
 * packageName : kr.xit.core.spring.filter
 * fileName    : ExceptionHandlerFilter
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see OncePerRequestFilter
 */
@Slf4j
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final Environment env;

    public ExceptionHandlerFilter(Environment env) {
        this.env = env;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        } catch (BizRuntimeException ex){
            log.error("exception exception handler filter");
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,response,ex);
        }catch (RuntimeException ex){
            log.error("runtime exception exception handler filter");
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,response,ex);
        }
    }

    private void setErrorResponse(HttpStatus status, HttpServletResponse response,Throwable ex){
        response.setStatus(status.value());
        response.setContentType("application/json");
        RestApiErrorResponse errorResponse = RestApiErrorResponse.getErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);

        // 운영 환경인 경우는 상세 정보 미출력
        if(Arrays.asList(env.getActiveProfiles()).contains("prod"))
            errorResponse.setMessage(Checks.isNotEmpty(ex.getCause())? ex.getLocalizedMessage() : ex.getCause().getLocalizedMessage());
        else
            errorResponse.setMessage(Checks.isNotEmpty(ex.getCause())? ex.getLocalizedMessage() : ex.getCause().toString());
        try{
            String json = errorResponse.convertToJson();
            log.error(json);
            response.getWriter().write(json);
        }catch (IOException e){
            log.error("ExceptionHandlerFilter::setErrorResponse", e);
        }

    }
}

