package kr.xit.core.oauth.jwt;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import egovframework.com.cmm.ResponseCode;
import kr.xit.core.api.RestApiResponse;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        Object errCode = request.getAttribute("code");
        Object message = request.getAttribute("message");

        RestApiResponse apiResult = RestApiResponse.result();
        apiResult.setSuccess(false);
        apiResult.setCode(errCode != null? errCode.toString() : String.valueOf(ResponseCode.AUTH_UNAUTHORIZED.getCode()));
        apiResult.setMessage(message != null? message.toString() : ResponseCode.AUTH_UNAUTHORIZED.getMessage());

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().print(apiResult.convertToJson());
    }
}
