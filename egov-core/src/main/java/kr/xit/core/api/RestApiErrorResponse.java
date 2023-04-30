package kr.xit.core.api;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


import io.swagger.v3.oas.annotations.media.Schema;
import kr.xit.core.ErrorCode;
import kr.xit.core.exception.BizRuntimeException;
import kr.xit.core.support.utils.Checks;
import kr.xit.core.support.utils.ConvertHelper;
import kr.xit.core.support.utils.DateUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * description : Api 에러 응답
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
@Schema(name = "RestApiErrorResponse", description = "Restful API 에러", implementation = IRestApiResponse.class)
@JacksonXmlRootElement(localName = "result")
@Slf4j
@Getter
@Builder
@ToString
public class RestApiErrorResponse implements IRestApiResponse, Serializable {
    private static final long SerialVersionUID = 1L;

    @Schema(name = "true: 성공, false:실패", example = "false", requiredMode = Schema.RequiredMode.REQUIRED, description = "에러인 경우 false")
    private final boolean success = false;

    @Schema(name = "에러 발생 시간", example = " ", description = "에러 발생 시간")
    private final String timestamp = DateUtils.getTodayAndNowTime("yyyy-MM-dd HH:mm:ss");

    @Schema(name = "HttpStatus 상태", example = " ", description = "HttpStatus 상태")
    private final int status;

    @Schema(name = "HttpStatus name", example = " ", description = "HttpStatus name")
    private final String error;

    @Schema(name = "코드(에러코드)", description = "코드(에러코드)")
    private final String code;

    @Schema(name = "에러 메세지", example = " ", description = "에러 메세지")
    @Setter
    private String message;

    public static ResponseEntity<? extends IRestApiResponse> of(ErrorCode errorCode) {
        RestApiErrorResponse errorResponse = getErrorResponse(errorCode);
        printErrorResponse(errorResponse);

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(errorResponse);
    }

    public static ResponseEntity<? extends IRestApiResponse> of(BizRuntimeException e) {
        RestApiErrorResponse errorResponse = null;

        if (Checks.isNotEmpty(e.getErrorCode())) {
            errorResponse = getErrorResponse(e.getErrorCode());
            return RestApiErrorResponse.of(e.getErrorCode());

        } else {
            errorResponse = RestApiErrorResponse.builder()
                    .status(e.getHttpStatus().value())
                    .error(e.getHttpStatus().name())
                    .code(Checks.isNotEmpty(e.getCode()) ? e.getCode() : StringUtils.EMPTY)
                    .message(Checks.isNotEmpty(e.getLocalizedMessage()) ? e.getLocalizedMessage() : e.getLocalizedMessage())
                    .build();
        }
        printErrorResponse(errorResponse);

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(errorResponse);
    }

    public static ResponseEntity<? extends IRestApiResponse> of(String code, String message) {
        RestApiErrorResponse errorResponse = RestApiErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.name())
                .code(code)
                .message(message)
                .build();
        printErrorResponse(errorResponse);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    public static RestApiErrorResponse getErrorResponse(ErrorCode errorCode) {
        return RestApiErrorResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .error(errorCode.getHttpStatus().name())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

    public String convertToJson() {
        return ConvertHelper.jsonToObject(this);
    }


    private static void printErrorResponse(RestApiErrorResponse errorResponse) {
        log.error("##############################################################################################");
        log.error("{}", errorResponse);
        log.error("##############################################################################################");
    }
}
