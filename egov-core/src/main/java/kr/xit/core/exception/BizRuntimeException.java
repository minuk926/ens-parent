package kr.xit.core.exception;

import java.util.Locale;

import org.egovframe.rte.fdl.cmmn.exception.BaseRuntimeException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import kr.xit.core.consts.ErrorCode;
import kr.xit.core.spring.annotation.SecurityPolicy;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * description : egov BaseRuntimeException 상속
 * packageName : kr.xit.core.exception
 * fileName    : BizRuntimeException
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see BaseRuntimeException
 */
@Getter
@Setter
@Slf4j
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid parameter")
public class BizRuntimeException extends BaseRuntimeException {

    private static final long serialVersionUID = 1L;
    private String code;
    private ErrorCode errorCode;

    /**
     * BizRuntimeException 생성자.
     * @param errorCode ErrorCode 지정
     */
    public static BizRuntimeException create(ErrorCode errorCode) {
        return new BizRuntimeException(errorCode);
    }

    /**
     * BizRuntimeException 생성자.
     * @param errorCode ErrorCode 지정
     */
    private BizRuntimeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = String.valueOf(errorCode.getHttpStatus().value());
        this.errorCode = errorCode;
    }
    
    /**
     * BizRuntimeException 생성자.
     * @param defaultMessage 메세지 지정
     */
    public static BizRuntimeException create(String defaultMessage) {
        return new BizRuntimeException(defaultMessage, null, null);
    }

    /**
     * BizRuntimeException 생성자.
     * @param wrappedException  원인 Exception
     */
    public static BizRuntimeException create(Throwable wrappedException) {
        return new BizRuntimeException("BizRuntimeException without message", null, wrappedException);
    }

    /**
     * BizRuntimeException 생성자.
     * @param defaultMessage 메세지 지정
     * @param wrappedException 원인 Exception
     */
    public static BizRuntimeException create(String defaultMessage, Throwable wrappedException) {
        return new BizRuntimeException(defaultMessage, null, wrappedException);
    }

    /**
     * BizRuntimeException 생성자.
     * @param defaultMessage 메세지 지정(변수지정)
     * @param messageParameters 치환될 메세지 리스트
     * @param wrappedException 원인 Exception
     */
    private static BizRuntimeException create(String defaultMessage, Object[] messageParameters, Throwable wrappedException) {
        return new BizRuntimeException(defaultMessage, messageParameters, wrappedException);
    }

    /**
     * BizRuntimeException 기본 생성자.
     */
    private BizRuntimeException() {
        this("BizRuntimeException without message", null, null);
    }

    /**
     * BizRuntimeException 생성자.
     * @param defaultMessage 메세지 지정(변수지정)
     * @param messageParameters 치환될 메세지 리스트
     * @param wrappedException 원인 Exception
     */
    private BizRuntimeException(String defaultMessage, Object[] messageParameters, Throwable wrappedException) {
        super(defaultMessage, messageParameters, wrappedException);
    }

    /**
     * BizRuntimeException 생성자.
     * @param messageSource 메세지 리소스
     * @param messageKey 메세지키값
     */
    public static BizRuntimeException create(MessageSource messageSource, String messageKey) {
        return new BizRuntimeException(messageSource, messageKey, null, null, Locale.getDefault(), null);
    }

    /**
     * BizRuntimeException 생성자.
     * @param messageSource 메세지 리소스
     * @param messageKey 메세지키값
     */
    public  static BizRuntimeException create(MessageSource messageSource, String messageKey, Throwable wrappedException) {
        return new BizRuntimeException(messageSource, messageKey, null, null, Locale.getDefault(), wrappedException);
    }

    /**
     * BizRuntimeException 생성자.
     * @param messageSource 메세지 리소스
     * @param messageKey 메세지키값
     * @param locale 국가/언어지정
     * @param wrappedException 원인 Exception
     */
    public static BizRuntimeException create(MessageSource messageSource, String messageKey, Locale locale, Throwable wrappedException) {
        return new BizRuntimeException(messageSource, messageKey, null, null, locale, wrappedException);
    }

    /**
     * BizRuntimeException 생성자.
     * @param messageSource 메세지 리소스
     * @param messageKey 메세지키값
     * @param messageParameters 치환될 메세지 리스트
     * @param locale 국가/언어지정
     * @param wrappedException 원인 Exception
     */
    public static BizRuntimeException create(MessageSource messageSource, String messageKey, Object[] messageParameters, Locale locale, Throwable wrappedException) {
        return new BizRuntimeException(messageSource, messageKey, messageParameters, null, locale, wrappedException);
    }

    /**
     * BizRuntimeException 생성자.
     * @param messageSource 메세지 리소스
     * @param messageKey 메세지키값
     * @param messageParameters 치환될 메세지 리스트
     * @param wrappedException 원인 Exception
     */
    public static BizRuntimeException create(MessageSource messageSource, String messageKey, Object[] messageParameters, Throwable wrappedException) {
        return new BizRuntimeException(messageSource, messageKey, messageParameters, null, Locale.getDefault(), wrappedException);
    }

    /**
     * BizRuntimeException 생성자.
     * @param messageSource 메세지 리소스
     * @param messageKey 메세지키값
     * @param messageParameters 치환될 메세지 리스트
     * @param defaultMessage 메세지 지정(변수지정)
     * @param wrappedException 원인 Exception
     */
    public static BizRuntimeException create(MessageSource messageSource, String messageKey, Object[] messageParameters, String defaultMessage, Throwable wrappedException) {
        return new BizRuntimeException(messageSource, messageKey, messageParameters, defaultMessage, Locale.getDefault(), wrappedException);
    }

    public BizRuntimeException(MessageSource messageSource, String messageKey, Object[] messageParameters, String defaultMessage, Locale locale, Throwable wrappedException) {
        super(messageSource, messageKey, messageParameters, defaultMessage, locale, wrappedException);
    }

    public HttpStatus getHttpStatus(){
        return HttpStatus.BAD_REQUEST;
    };
}
