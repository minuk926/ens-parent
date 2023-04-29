package kr.xit.core.spring.config.auth.jwt;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import egovframework.com.cmm.ResponseCode;
import kr.xit.core.Constants;
import kr.xit.core.ErrorCode;
import kr.xit.core.exception.BizRuntimeException;
import kr.xit.core.support.utils.Checks;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws
        ServletException,
        IOException {



        final String TOKEN_PREFIX = Constants.JwtToken.GRANT_TYPE.getCode();
        String authToken = req.getHeader(Constants.JwtToken.HEADER_NAME.getCode());

        if(Checks.isNotEmpty(authToken)) {

            if(!Pattern.matches(String.format("^%s .*", TOKEN_PREFIX), authToken)){
                throw BizRuntimeException.create("유효하지 않은 Token 입니다.");
            }
            authToken = authToken.replaceAll(String.format("^%s ()*", TOKEN_PREFIX), "");

            try {
                Authentication authentication = jwtTokenProvider.getAuthentication(authToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (BizRuntimeException e) {
                if(Checks.isNotEmpty(e.getErrorCode())){
                    ErrorCode errorCode = e.getErrorCode();
                    req.setAttribute("code", String.valueOf(errorCode.getHttpStatus().value()));
                    req.setAttribute("message", errorCode.getMessage());
                }
            } catch (Exception e){
                //e.printStackTrace();
                req.setAttribute("code", String.valueOf(ResponseCode.AUTH_UNAUTHORIZED.getCode()));
                req.setAttribute("message", ResponseCode.AUTH_UNAUTHORIZED.getMessage());
            }

        }
        chain.doFilter(req, res);
    }

    //
    // @Override
    // protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws
    //     ServletException,
    //     IOException {
    //     String header = req.getHeader(HEADER_STRING);
    //     String userId = null;
    //     String auth = null;
    //     String authToken = null;
    //     if (header != null) {
    //         authToken = header;
    //
    //         if(!Pattern.matches("^Bearer .*", authToken)){
    //             throw BizRuntimeException.create("유효하지 않은 Token 입니다.");
    //         }
    //         authToken = authToken.replaceAll("^Bearer ()*", "");
    //         Authentication authentication = null;
    //
    //         try {
    //             // Claims claims = jwtTokenUtil.getClaimFromToken(authToken);
    //             // userId = String.valueOf(claims.get("userId"));
    //             // auth = claims.get);
    //             authentication = jwtTokenProvider.getAuthentication(authToken);
    //
    //         } catch (BizRuntimeException e) {
    //             if(Checks.isNotEmpty(e.getErrorCode())){
    //                 ErrorCode errorCode = e.getErrorCode();
    //                 req.setAttribute("code", String.valueOf(errorCode.getHttpStatus().value()));
    //                 req.setAttribute("message", errorCode.getMessage());
    //             }
    //         } catch (Exception e){
    //             e.printStackTrace();
    //             req.setAttribute("code", String.valueOf(ResponseCode.AUTH_UNAUTHORIZED.getCode()));
    //             req.setAttribute("message", ResponseCode.AUTH_UNAUTHORIZED.getMessage());
    //         }
    //     }
    //     if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null){
    //
    //         boolean isValid = false;
    //         String ROLE_KEY = "";
    //
    //         if(auth.equals("")){
    //             isValid = jwtTokenProvider.validateTokenExcludeExpired(authToken, false, false);
    //             ROLE_KEY = "ROLE_USER";
    //         } else if(auth.equals("AD"))   {
    //             isValid = jwtTokenProvider.validateTokenExcludeExpired(authToken);
    //             ROLE_KEY = "ROLE_ADMIN";
    //         }
    //
    //         if (isValid) {
    //             UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authToken, null, Arrays.asList(new SimpleGrantedAuthority(
    //                 Constants.JwtToken.AUTHORITIES_KEY.getCode())));
    //             authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
    //             log.info("authenticated user " + userId + ", setting security context");
    //             SecurityContextHolder.getContext().setAuthentication(authentication);
    //         }
    //     }
    //     chain.doFilter(req, res);
    // }
}
