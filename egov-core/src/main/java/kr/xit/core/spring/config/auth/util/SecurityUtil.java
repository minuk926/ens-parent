package kr.xit.core.spring.config.auth.util;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import kr.xit.core.consts.Constants;
import kr.xit.core.consts.ErrorCode;
import kr.xit.core.exception.BizRuntimeException;
import kr.xit.core.support.utils.Checks;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * description : Security Utitlity
 *               JwtTokenProviderr에서 저장한 유저정보(토큰)에서 사용자 ID를 GET
 * packageName : kr.xit.core.spring.config.auth.util
 * fileName    : SecurityUtil
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
public class SecurityUtil {

    private SecurityUtil() { }

    /**
     *  OAuth2LocalController#login
     * @return String
     */
    // SecurityContext 에 유저 정보가 저장되는 시점
    // Request 가 들어올 때 JwtFilter 의 doFilter 에서 저장
    public static String getCurrentUserId(@NotNull final String authType) {

        // TODO :: SessionCreationPolicy.STATELESS 인 경우 사용 불가
        if(Objects.equals(authType, Constants.AuthSaveType.SECURITY.getCode())) {
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getName() == null) {
                throw BizRuntimeException.create(ErrorCode.NOT_EXISTS_SECURITY_AUTH);
            }
            return authentication.getName();

        // TODO :: Session에 저장한 경우 - Token도 가능은 하지만...
        }else if(Objects.equals(authType, Constants.AuthSaveType.SESSION.getCode())) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            HttpSession session = request.getSession();
            Object o = session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            if(Checks.isNotNull(o)) {
                SecurityContextImpl sci = (SecurityContextImpl) o;
                Authentication authentication = sci.getAuthentication();

                if (authentication == null || authentication.getName() == null) {
                    throw BizRuntimeException.create(ErrorCode.NOT_EXISTS_SECURITY_AUTH);
                }
                return authentication.getName();
            }else {
                throw BizRuntimeException.create(ErrorCode.NOT_EXISTS_SECURITY_AUTH);
            }
        }else if(Objects.equals(authType, Constants.AuthSaveType.HEADER.getCode())) {
            return HeaderUtil.getUserId();

        }else{
            throw BizRuntimeException.create(ErrorCode.UN_AUTHORIZED_USER);
        }
    }
}
