// package kr.xit.core.spring.config.auth.jwt;
//
// import java.io.IOException;
//
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
//
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.security.web.access.AccessDeniedHandler;
// import org.springframework.stereotype.Component;
// import org.springframework.web.servlet.HandlerExceptionResolver;
//
// import lombok.RequiredArgsConstructor;
//
// /**
//  * <pre>
//  * description : TokenAccessDeniedHandler
//  * packageName : kr.xit.core.spring.config.auth.jwt
//  * fileName    : TokenAccessDeniedHandler
//  * author      : julim
//  * date        : 2023-04-28
//  * ======================================================================
//  * 변경일         변경자        변경 내용
//  * ----------------------------------------------------------------------
//  * 2023-04-28    julim       최초 생성
//  *
//  * </pre>
//  */
// @Component
// @RequiredArgsConstructor
// public class TokenAccessDeniedHandler implements AccessDeniedHandler {
//
//     private final HandlerExceptionResolver handlerExceptionResolver;
//
//     @Override
//     public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
//         //response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedExceptiongetLocalizedMessage());
//         handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
//     }
// }
