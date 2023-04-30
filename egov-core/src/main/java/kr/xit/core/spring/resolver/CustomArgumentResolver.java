package kr.xit.core.spring.resolver;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * description :  Dispatchersevlet 이 요청 전달시 컨트롤러에서 필요로 하는 객체 생성 및 바인딩
 *                아래의 어노테이션이 ArgumentResolver로 동작
 *                * @RequestParam: 쿼리 파라미터 값 바인딩
 *                * @ModelAttribute: 쿼리 파라미터 및 폼 데이터 바인딩
 *                * @CookieValue: 쿠키값 바인딩
 *                * @RequestHeader: 헤더값 바인딩
 *                * @RequestBody: 바디값 바인딩
 * packageName : kr.xit.core.spring.resolver
 * fileName    : CustomArgumentResolver
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see HandlerMethodArgumentResolver
 */
// FIXME:: CustomArgumentResolver
@Slf4j
public class CustomArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return true;
        //return parameter.getParameterType().equals(User.class);

        // parameter.hasParameterAnnotation(User.class)
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory) throws Exception {
        // HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        //
        // String token = JwtUtil.extract(httpServletRequest);
        // JwtUtil.validateToken(token);
        //
        // String userId = JwtUtil.getPayload(token);
        // String ipAddress = httpServletRequest.getRemoteAddr();
        //
        // return new User(userId, ipAddress);
        System.out.println("~~~CustomArgumentResolver~~~~");
        return null;
    }
}
