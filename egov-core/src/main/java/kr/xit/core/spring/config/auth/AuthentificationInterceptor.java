package kr.xit.core.spring.config.auth;

import kr.xit.core.consts.Constants;
import kr.xit.core.consts.ErrorCode;
import kr.xit.core.exception.BizRuntimeException;
import kr.xit.core.spring.annotation.Secured;
import kr.xit.core.spring.annotation.SecurityPolicy;
import kr.xit.core.spring.config.auth.util.HeaderUtil;
import kr.xit.core.spring.util.SpringUtils;
import kr.xit.core.support.utils.Checks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * <pre>
 * description : 인증 처리 Interceptor
 *              - annotation 방식
 *              - @Secured(policy = SecurityPolicy.TOKEN | SESSION | COOKIE)
 * packageName : kr.xit.core.spring.config.auth
 * fileName    : AuthentificationInterceptor
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
@Slf4j
public class AuthentificationInterceptor implements AsyncHandlerInterceptor {//AsyncHandlerInterceptor { //extends HandlerInterceptorAdapter{ // HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        log.info("AuthentificationInterceptor start ==>> ");

        SecurityPolicy policy = null;

        if(handler instanceof HandlerMethod){
            policy = SecurityPolicy.DEFAULT;

            HandlerMethod method = (HandlerMethod)handler;
            Secured secured = method.getMethod().getAnnotation(Secured.class);

            if(secured != null){
                policy = secured.policy();
            }else{
                secured = method.getBeanType().getAnnotation(Secured.class);
                if(secured != null)
                    policy = secured.policy();
            }
        }

        if(Checks.isNotEmpty(policy)){

            // 토큰 인증
            if (Objects.equals(SecurityPolicy.TOKEN, policy)) {
                log.debug("TOKEN 인증 start ==>> ");

                String tokenString = request.getHeader(Constants.JwtToken.HEADER_NAME.getCode());

                if(Checks.isNotEmpty(tokenString)){

                    try{

                        // Spring Security 미사용
                        tokenString = HeaderUtil.extractAccessToken(tokenString);
                        if(SpringUtils.getJwtVerification().isVerification(tokenString)){
                            throw BizRuntimeException.create(ErrorCode.INVALID_AUTH_TOKEN);
                        };
                        return true;

                        // Spring Security 사용시
                        // if(SpringUtils.getJwtTokenProvider().validateTokenExcludeExpired(tokenString, false, true)){
                        //     log.debug("<<==== 토큰인증성공");
                        //     return true;
                        // }


                    }catch(BizRuntimeException cbe){
                        //TODO Refresh토큰 사용시 자동 재발급하는 경우 주석 제거
                        // access token expired >> refresh 토큰 확인후 access 토큰 재발급
                        //						if(Objects.equals(ErrorConst.TOKEN_EXPIRED, be.getErrorCode())){
                        //							//TODO refresh 토큰유효기간 확인
                        //							// 1.GET refresh 토큰
                        //
                        //							// 2.refresh 토큰 유효성 검사
                        //							if(tokenService.isValidToken("", tokenString)){
                        //								tokenService.getRefreshToken(tokenString, response);
                        //								return true;
                        //
                        //							}else{
                        //								throw new BaseException("swork.token.expired", new String[]{}, ErrorConst.TOKEN_EXPIRED);
                        //							}
                        //						}
                        log.error("====토큰인증실패 :: {} ====", cbe.getLocalizedMessage());
                        throw cbe;
                    }
                }else{
                    log.error("==== 토큰인증실패 :: 요청 헤더에 토큰이 존재하지 않습니다. ====");
                    throw BizRuntimeException.create(ErrorCode.AUTH_HEADER_NOT_EXISTS);
                }

                // 세션 인증 (cookie 테스트를 위한 redirect 설정)
            }else if (Objects.equals(SecurityPolicy.SESSION, policy)) {

                return true;
                // 쿠키 인증 데이터 생성
            }else if (Objects.equals(SecurityPolicy.COOKIE, policy)) {

                return true;
            }
        }else{
            //log.debug("<<== OpenApiTokenInterceptor end");
            return true;
        }

        // TODO : 인증이 필요없는 Page?? 로 처리할지 에러(401:인가되지 않음)로 표시할지 결정
        // 접근불가로 처리하려면 주석처리
        if(Objects.equals(SecurityPolicy.DEFAULT, policy)){
            //log.debug("===================== 인증이 필요없는 페이지 ===========================");
            return true;
        }
        // 401 : 인가되지 않음으로 에러 처리
        log.error("====인가되지 않음====");
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        throw BizRuntimeException.create(ErrorCode.INVALID_AUTH_TOKEN);
        //return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception{

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception{

    }
}
