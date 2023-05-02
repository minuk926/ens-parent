// package kr.xit.core.spring.config.auth.jwt;
//
// import java.io.IOException;
// import java.io.Serializable;
//
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
//
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.web.AuthenticationEntryPoint;
// import org.springframework.stereotype.Component;
//
// import kr.xit.core.api.RestApiResponse;
// import kr.xit.core.consts.ErrorCode;
// import lombok.extern.slf4j.Slf4j;
//
// /**
//  * <pre>
//  * description : JwtAuthenticationEntryPoint
//  * packageName : kr.xit.core.spring.config.auth.jwt
//  * fileName    : JwtAuthenticationEntryPoint
//  * author      : julim
//  * date        : 2023-04-28
//  * ======================================================================
//  * 변경일         변경자        변경 내용
//  * ----------------------------------------------------------------------
//  * 2023-04-28    julim       최초 생성
//  *
//  * </pre>
//  */
// @Slf4j
// @Component
// public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
//
//     @Override
//     public void commence(HttpServletRequest request,
//                          HttpServletResponse response,
//                          AuthenticationException authException) throws IOException {
//
//         //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//         Object errCode = request.getAttribute("code");
//         Object message = request.getAttribute("message");
//
//         RestApiResponse apiResult = RestApiResponse.result();
//         apiResult.setSuccess(false);
//         apiResult.setCode(errCode != null? errCode.toString() : String.valueOf(ErrorCode.UN_AUTHORIZED_USER.getHttpStatus().value()));
//         apiResult.setMessage(message != null? message.toString() : ErrorCode.UN_AUTHORIZED_USER.getMessage());
//
//         response.setContentType("application/json;charset=UTF-8");
//         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//         response.getWriter().print(apiResult.convertToJson());
//     }
// }
