package kr.xit.core.spring;

import java.lang.reflect.Type;

import org.apache.ibatis.session.RowBounds;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * RequestBodyAdvice는 요청 body를 커스터마이징
 *
 * supports: 해당 RequestBodyAdvice를 적용할지 여부를 결정함
 * beforeBodyRead: body를 읽어 객체로 변환되기 전에 호출됨
 * afterBodyRead: body를 읽어 객체로 변환된 후에 호출됨
 * handleEmptyBody: body가 비어있을때 호출됨
 * </pre>
 */
// FIXME:: RequestBody cutomizing
@RestControllerAdvice
@Slf4j
public class RequestBodyControllerAdvice implements RequestBodyAdvice {

    @Override
    public boolean supports(
        final MethodParameter methodParameter,
        final Type targetType,
        final Class<? extends HttpMessageConverter<?>> converterType) {
        //return true;
        //return methodParameter.getContainingClass() == UserController.class && targetType.getTypeName().equals(User.class.getTypeName());
        log.debug("{}", methodParameter.getParameterName());
        log.debug("{}", targetType);
        log.debug("{}", converterType);
        return methodParameter.getContainingClass() == RowBounds.class && targetType.getTypeName().equals(RowBounds.class.getTypeName());
    }

    @Override
    public HttpInputMessage beforeBodyRead(
        final HttpInputMessage inputMessage,
        final MethodParameter parameter,
        final Type targetType,
        final Class<? extends HttpMessageConverter<?>> converterType) {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(
        final Object body,
        final HttpInputMessage inputMessage,
        final MethodParameter parameter,
        final Type targetType,
        final Class<? extends HttpMessageConverter<?>> converterType) {
        // final User user = (User) body;
        // user.setDesc("desc");
        // return user;
        log.debug("{}", body);
        return body;
    }

    @Override
    public Object handleEmptyBody(
        final Object body,
        final HttpInputMessage inputMessage,
        final MethodParameter parameter,
        final Type targetType,
        final Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

}
