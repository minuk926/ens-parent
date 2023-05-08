package kr.xit.ens.support.common.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * <pre>
 * description :
 * packageName : kr.xit.ens.support.common.code
 * fileName    : KkoReponseCode
 * author      : minuk
 * date        : 2023/05/07
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023/05/07   minuk       최초 생성
 *
 * </pre>
 */
public class KkoReponseCode {

    public enum ErrorCode {

//        INVALID_VALUE("INVALID_VALUE", "INVALID_VALUE", "유효하지 않은 값입니다."),
//        UNIDENTIFIED_USER("UNIDENTIFIED_USER", "INVALID_VALUE", "유효하지 않은 값입니다."),
//        UNAUTHORIZED("UNAUTHORIZED", "UNAUTHORIZED", "접근 권한이 없습니다."),
//        FORBIDDEN("FORBIDDEN", "FORBIDDEN", "허용되지 않는 요청입니다. 수신거부된 사용자 입니다."),
//        NOT_FOUND("NOT_FOUND", "NOT_FOUND", "요청 정보를 찾을 수 없습니다."),
//        INTERNAL_ERROR("INTERNAL_ERROR", "INTERNAL_SERVER_ERROR", "서버 에러입니다. 다시 시도해 주세요."),
//        ;

        INVALID_VALUE(400, "INVALID_VALUE", "유효하지 않은 값입니다."),
        UNIDENTIFIED_USER(400, "INVALID_VALUE", "유효하지 않은 값입니다."),
        UNAUTHORIZED(401, "UNAUTHORIZED", "접근 권한이 없습니다."),
        FORBIDDEN(403, "FORBIDDEN", "허용되지 않는 요청입니다. 수신거부된 사용자 입니다."),
        NOT_FOUND(404, "NOT_FOUND", "요청 정보를 찾을 수 없습니다."),
        INTERNAL_ERROR(500, "INTERNAL_SERVER_ERROR", "서버 에러입니다. 다시 시도해 주세요."),
        ;

        @Getter
        private int errorCode;

        @Getter
        private String errorString;

        @Getter
        private String message;

        ErrorCode(int errorCode, String errorString, String message){
            this.errorCode = errorCode;
            this.errorString = errorString;
            this.message = message;
        }
    }
}
