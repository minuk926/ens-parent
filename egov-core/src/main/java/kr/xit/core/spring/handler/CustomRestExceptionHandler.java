package kr.xit.core.spring.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.egovframe.rte.fdl.cmmn.exception.EgovBizException;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import kr.xit.core.consts.ErrorCode;
import kr.xit.core.api.IRestApiResponse;
import kr.xit.core.api.RestApiErrorResponse;
import kr.xit.core.exception.BizRuntimeException;
import kr.xit.core.support.utils.Checks;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * ExceptionHandlerFilter(Filter에서 발생한 에러 처리)와 함께 에러 처리
 * ErrorCode 에서 해당 Exception 오류를 정의하여 사용
 *
 * spring boot의 기본 properties
 * server.error:
 *   include-exception: false # 응답에 exception의 내용을 포함할지 여부
 *   include-stacktrace: never # 오류 응답에 stacktrace 내용을 포함할 지 여부
 *   path: '/error' # 오류 응답을 처리할 Handler의 경로
 *   whitelabel.enabled: true # 서버 오류 발생시 브라우저에 보여줄 기본 페이지 생성 여부
 *   </pre>
 */
/**
 * <pre>
 * description : ExceptionHandlerFilter(Filter에서 발생한 에러 처리)와 함께 에러 처리
 *               ErrorCode 에서 해당 Exception 오류를 정의하여 사용
 *               - spring boot의 기본 properties
 *                server.error:
 *                  include-exception: false  # 응답에 exception의 내용을 포함할지 여부
 *                  include-stacktrace: never # 오류 응답에 stacktrace 내용을 포함할 지 여부
 *                  path: '/error'            # 오류 응답을 처리할 Handler의 경로
 *                  whitelabel.enabled: true  # 서버 오류 발생시 브라우저에 보여줄 기본 페이지 생성 여부
 * packageName : kr.xit.core.spring.handler
 * fileName    : CustomRestExceptionHandler
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see ResponseEntityExceptionHandler
 */
@Slf4j
@RestControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * BizRuntimeException
     *
     * @param e BizRuntimeException
     * @return ErrorApiResponse
     */
    @ExceptionHandler(value = {BizRuntimeException.class})
    protected ResponseEntity<? extends IRestApiResponse> handleBizRutimeException(BizRuntimeException e) {
        log.error("==== throw Exception : {} ====", e.getClass().getCanonicalName());
        return RestApiErrorResponse.of(e);
    }

    /**
     * EgovBizException
     *
     * @param e EgovBizException
     * @return ErrorApiResponse
     */
    @ExceptionHandler(value = {EgovBizException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<? extends IRestApiResponse> handleEgovBizException(EgovBizException e) {
        log.error("==== throw Exception : {} ====", e.getClass().getCanonicalName());
        return RestApiErrorResponse.of(String.valueOf(HttpStatus.BAD_REQUEST.value()), e.getMessage());
    }

    /**
     * MethodArgumentNotValidException 에러 메세지 처리
     * Valid 체크 에러 메세지 처리를 위한 ResponseEntityExceptionHandler#handleMethodArgumentNotValid override
     *
     * Customize the response for MethodArgumentNotValidException.
     * <p>This method delegates to {@link #handleExceptionInternal}.
     * @param ex the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @SuppressWarnings("NullableProblems")
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("==== throw Exception : {} ====", ex.getClass().getCanonicalName());

        Map<String, String> validErrorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> validErrorMap.put(e.getField(), e.getDefaultMessage()));
        return RestApiErrorResponse.getResponseEntity(validErrorMap.toString());
        /*
        Optional<String> firstKey = validErrorMap
                .keySet()
                .stream()
                .findFirst();


        Optional<String> firstMessage = validErrorMap
                .values()
                .stream()
                .findFirst();

        String errMsg = "["+firstKey.orElse("에러 메세지가 정의 되지 않았습니다")+"] "+firstMessage.orElse("");
        */
    }

    /**
     * Customize the response for HttpMessageNotReadableException.
     * <p>This method delegates to {@link #handleExceptionInternal}.
     * @param ex the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @SuppressWarnings("NullableProblems")
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("==== throw Exception : {} ====", ex.getClass().getCanonicalName());

        return RestApiErrorResponse.getResponseEntity(ex.getLocalizedMessage());
    }

   /**
     * NoSuchElementException
     *
     * @return ErrorResponse
     */
    @ExceptionHandler(value = {NoSuchElementException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<? extends IRestApiResponse> handleNoSuchElementException() {
        log.error("==== throw NoSuchElementException ====");
        return RestApiErrorResponse.of(ErrorCode.SQL_DATA_RESOURCE_INVALID);
    }

    /**
     * NoSuchElementException
     *
     * @return ErrorResponse
     */
    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<? extends IRestApiResponse> handleIllegalArgumentException(IllegalArgumentException iae) {
        log.error("==== throw Exception : {} ====", iae.getClass().getCanonicalName());
        return RestApiErrorResponse.of(HttpStatus.BAD_REQUEST.toString(), iae.getLocalizedMessage());
    }

    /**
     * Data 중복
     *
     * @return ErrorResponse
     */
    @ExceptionHandler(value = {ConstraintViolationException.class, DataIntegrityViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<? extends IRestApiResponse> handleDataException() {
        log.error("==== throw ConstraintViolationException, DataIntegrityViolationException ====================");
        return RestApiErrorResponse.of(ErrorCode.SQL_DATA_RESOURCE_INVALID);
    }

    /**
     * RuntimeException
     *
     * @param e RuntimeException
     * @return ErrorResponse
     */
    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<? extends IRestApiResponse> handleRuntimeException(RuntimeException e) {
        String message = Checks.isNotNull(e) ? e.getLocalizedMessage() : StringUtils.EMPTY;
        log.error("handleException RuntimeException : {}", Checks.isEmpty(message) ? StringUtils.EMPTY : e.getClass().getCanonicalName());

        // Hibernate SQL 예외인 경우 메세지 획득
//        if(e instanceof PersistenceException || e instanceof JDBCException || e instanceof JpaSystemException || e instanceof GenericJDBCException){
//            message = ((GenericJDBCException) e.getCause()).getSQLException().getLocalizedMessage();
//            //message = ((GenericJDBCException) e.getCause()).getSQLException().getLocalizedMessage();
//        }
        return RestApiErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.toString(), message);
    }

    /**
     * Exception
     *
     * @param e Exception
     * @return ErrorResponse
     */
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<? extends IRestApiResponse> handleException(Exception e) {
        String message = Checks.isNotNull(e) ? e.getLocalizedMessage() : StringUtils.EMPTY;
        log.error("==== throw Exception : {}", Checks.isEmpty(message) ? StringUtils.EMPTY : e.getClass().getCanonicalName());
        return RestApiErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.toString(), message);
    }
}

/*
//TODO :: 공통 framework에 반영 필요
javax.validation.constraints @NotNull


@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ExceptionDto>> methodArgumentValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(extractErrorMessages(e));
    }

    private List<ExceptionDto> extractErrorMessages(MethodArgumentNotValidException e) {
        return e.getBindingResult()
            .getAllErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .map(ExceptionDto::new)
            .collect(Collectors.toList());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<ExceptionDto>> constraintViolationException(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(extractErrorMessages(e));
    }

    private List<ExceptionDto> extractErrorMessages(ConstraintViolationException e) {
        return e.getConstraintViolations()
            .stream()
            .map(ConstraintViolation::getMessage)
            .map(ExceptionDto::new)
            .collect(Collectors.toList());
    }
 */
