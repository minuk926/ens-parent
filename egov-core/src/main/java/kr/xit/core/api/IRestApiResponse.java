package kr.xit.core.api;

import javax.servlet.http.HttpServletResponse;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <pre>
 * description : Api 응답 인터페이스
 * packageName : kr.xit.core.api
 * fileName    : IRestApiResponse
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
@Schema(required = true, name = "IRestApiResponse", description = "Rest Api Response interface class", implementation = RestApiResponse.class)
public interface IRestApiResponse {
    public enum ResponseCode {

        SUCCESS(200, "성공했습니다."),
        // 401
        AUTH_UNAUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "인증 되지 않은 사용자 입니다."),
        // 403
        AUTH_ERROR(HttpServletResponse.SC_FORBIDDEN, "인가된 사용자가 아닙니다."),
        DELETE_ERROR(700, "삭제 중 내부 오류가 발생했습니다."),
        SAVE_ERROR(800, "저장시 내부 오류가 발생했습니다."),
        INPUT_CHECK_ERROR(900, "입력값 무결성 오류 입니다."),
        EMPTY_MESSAGE(999, "정의되지 않은 코드(메세지) 입니다.")
        ;

        private int code;
        private String message;

        private ResponseCode(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }




    }
}
