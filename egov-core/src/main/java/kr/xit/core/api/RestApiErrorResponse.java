package kr.xit.core.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


import io.swagger.v3.oas.annotations.media.Schema;
import kr.xit.core.consts.ErrorCode;
import kr.xit.core.exception.BizRuntimeException;
import kr.xit.core.support.utils.Checks;
import kr.xit.core.support.utils.ConvertHelper;
import kr.xit.core.support.utils.DateUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;

/**
 * <pre>
 * description : Api 에러 응답
 *               error 로 return 되면 안됀다
 *               - ResponseEntity.ok()로 처리
 * packageName : kr.xit.core.api
 * fileName    : RestApiErrorResponse
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see IRestApiResponse
 */
//TODO :: refactoring 필요

@Schema(name = "RestApiErrorResponse", description = "Restful API 에러")//, implementation = IRestApiResponse.class)
@JacksonXmlRootElement(localName = "result")
@Slf4j
@Getter
@Builder
@ToString
public class RestApiErrorResponse implements IRestApiResponse, Serializable {
    private static final long SerialVersionUID = 1L;

    @Schema(example = "false", requiredMode = Schema.RequiredMode.REQUIRED, description = "성공여부 - 에러인 경우 false")
    private final boolean success = false;

    @Schema(example = " ", description = "에러 발생 시간")
    private final String timestamp = DateUtils.getTodayAndNowTime("yyyy-MM-dd HH:mm:ss");

    @Schema(example = " ", description = "HttpStatus 상태 코드")
    private final int statusCode;

//    @Schema(example = " ", description = "HttpStatus name")
//    private final String error;

    @Schema(description = "코드(에러코드)")
    private final String code;

    @Schema(example = " ", description = "에러 메세지")
    @Setter
    private String message;

//    @Schema(example = " ", description = "HttpStatus")
//    @Setter
//    private HttpStatus httpStatus;

    public static ResponseEntity<? extends IRestApiResponse> of(final String message) {
        RestApiErrorResponse errorResponse = RestApiErrorResponse.builder()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            //.error(HttpStatus.BAD_REQUEST.name())
            .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
            .message(message)
            .build();
        printErrorResponse(errorResponse);

        return ResponseEntity
            .ok()
            .body(errorResponse);
    }

    public static ResponseEntity<? extends IRestApiResponse> of(final ErrorCode errorCode) {
        RestApiErrorResponse errorResponse = getErrorResponse(errorCode);
        printErrorResponse(errorResponse);

        return ResponseEntity
                .ok()
                .body(errorResponse);
    }

    public static ResponseEntity<? extends IRestApiResponse> of(final BizRuntimeException e) {
        RestApiErrorResponse errorResponse = null;

        if (Checks.isNotEmpty(e.getErrorCode())) {
            errorResponse = getErrorResponse(e.getErrorCode());
            return RestApiErrorResponse.of(e.getErrorCode());

        } else {
            errorResponse = RestApiErrorResponse.builder()
                    .statusCode(e.getHttpStatus().value())
                    //.error(e.getHttpStatus().name())
                    .code(Checks.isNotEmpty(e.getCode()) ? e.getCode() : StringUtils.EMPTY)
                    .message(Checks.isNotEmpty(e.getLocalizedMessage()) ? e.getLocalizedMessage() : e.getLocalizedMessage())
                    .build();
        }
        printErrorResponse(errorResponse);

        return ResponseEntity
                .ok()
                .body(errorResponse);
    }

    public static ResponseEntity<? extends IRestApiResponse> of(final String code, final String message) {
        RestApiErrorResponse errorResponse = RestApiErrorResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                //.error(HttpStatus.BAD_REQUEST.name())
                .code(code)
                .message(message)
                .build();
        printErrorResponse(errorResponse);

        return ResponseEntity
                .ok()
                .body(errorResponse);
    }

    public static ResponseEntity<? extends IRestApiResponse> of(BindingResult bindingResult) {
        Map<String, String> validErrorMap = new HashMap<>();
        bindingResult.getFieldErrors()
                .forEach(e -> validErrorMap.put(e.getField(), e.getDefaultMessage()));

        RestApiErrorResponse errorResponse = RestApiErrorResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                //.error(HttpStatus.BAD_REQUEST.name())
                .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .message(validErrorMap.toString())
                .build();
        printErrorResponse(errorResponse);

        return ResponseEntity
                .ok()
                .body(errorResponse);
    }

    public static RestApiErrorResponse getErrorResponse(final ErrorCode errorCode) {
        return RestApiErrorResponse.builder()
                .statusCode(errorCode.getHttpStatus().value())
                //.error(errorCode.getHttpStatus().name())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

    public static ResponseEntity<Object> getResponseEntity(final String message) {
        RestApiErrorResponse errorResponse = RestApiErrorResponse.builder()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            //.error(HttpStatus.BAD_REQUEST.name())
            .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
            .message(message)
            .build();
        printErrorResponse(errorResponse);

        return ResponseEntity
            .ok()
            .body(errorResponse);
    }

    public String convertToJson() {
        return ConvertHelper.jsonToObject(this);
    }


    private static void printErrorResponse(final RestApiErrorResponse errorResponse) {
        log.error("##############################################################################################");
        log.error("{}", errorResponse);
        log.error("##############################################################################################");
    }
}
