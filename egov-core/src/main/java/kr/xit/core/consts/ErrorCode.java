package kr.xit.core.consts;

import org.egovframe.rte.fdl.cmmn.exception.BaseRuntimeException;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * description : HttpStatus 기준 에러 코드 정의
 * packageName : kr.xit.core.const
 * fileName    : ErrorCode
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see HttpStatus
 */
@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum
ErrorCode {

    /*
200 : OK, 요청 정상 처리
201 : Created, 생성 요청 성공
202 : Accepted, 비동기 요청 성공
204 : No Content, 요청 정상 처리, 응답 데이터 없음.

실패
400 : Bad Request, 요청이 부적절 할 때, 유효성 검증 실패, 필수 값 누락 등.
401 : Unauthorized, 인증 실패, 로그인하지 않은 사용자 또는 권한 없는 사용자 처리
402 : Payment Required
403 : Forbidden, 인증 성공 그러나 자원에 대한 권한 없음. 삭제, 수정시 권한 없음.
404 : Not Found, 요청한 URI에 대한 리소스 없을 때 사용.
405 : Method Not Allowed, 사용 불가능한 Method를 이용한 경우.
406 : Not Acceptable, 요청된 리소스의 미디어 타입을 제공하지 못할 때 사용.
408 : Request Timeout
409 : Conflict, 리소스 상태에 위반되는 행위 시 사용.
413 : Payload Too Large
423 : Locked
428 : Precondition Required
429 : Too Many Requests

500 : 서버 에러

     */


    BAD_REQUEST(HttpStatus.BAD_REQUEST, "요청 매개변수 오류 입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "잘못된 요청 입니다"),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일이 존재하지 않습니다"),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "처리 데이타 오류(처리 요청 데이타 미존재)"),

    /* 400 BAD_REQUEST : 잘못된 요청 */
    CANNOT_FOLLOW_MYSELF(HttpStatus.BAD_REQUEST, "자기 자신은 팔로우 할 수 없습니다"),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    //INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰입니다"),
    UN_AUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "계정 정보가 존재하지 않습니다"),
    AUTH_HEADER_NOT_EXISTS(HttpStatus.UNAUTHORIZED, "헤더에 인증 정보를 찾을 수 없습니다"),
    LOGOUT_USER(HttpStatus.UNAUTHORIZED, "로그아웃된 사용자 입니다"),
    NOT_EXISTS_SECURITY_AUTH(HttpStatus.UNAUTHORIZED, "Security Context 에 인증 정보가 없습니다"),

    NOT_EXISTS_TOKEN(HttpStatus.UNAUTHORIZED, "인증된 토큰이 없습니다"),
    NOT_EXISTS_SAVED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "저장된 인증 토큰이 없습니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효한 토큰이 아닙니다"),
    INVALID_ROLE_TOKEN(HttpStatus.UNAUTHORIZED, "사용 권한이 없는 토큰 입니다"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 유효기간이 경과 되었습니다"),
    INVALID_SIGN_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 서명의 토큰 입니다"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다"),
    MISMATCH_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰의 유저 정보가 일치하지 않습니다"),
    MISMATCH_REFRESH_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "발급된 refresh token의 정보가 일치하지 않습니다"),
    NOT_EXPIRED_TOKEN_YET(HttpStatus.UNAUTHORIZED, "토큰 유효기간이 경과되지 않았습니다"),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다"),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "로그아웃 된 사용자입니다"),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "데이터가 이미 존재합니다"),
    MEMBER_EXISTS(HttpStatus.CONFLICT, "가입되어 있는 회원 입니다"),

    // JPA query error
    SQL_DATA_RESOURCE_INVALID(HttpStatus.BAD_REQUEST, "JPA(Hibernate) SQL 오류(데이타 / 조건)"),

    MPOWER_CONNECT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "MPower DB 접속 에러 입니다"),
    MPOWER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "MPower DB 에러 입니다"),

    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN"),
    INVALID_CODE(HttpStatus.BAD_REQUEST, "유효하지 않은 HttpStatus 상태 코드 입니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"),

    MISMATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀 번호가 일치하지 않습니다.")
    ;


    //USER_NOT_FOUND(null, "사용자(%s) 정보를 찾을 수 없습니다");

    private HttpStatus httpStatus;
    private String message;
    //Integer code;

    ErrorCode(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.message = message;
    }

    ErrorCode(String message){
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.message = message;
    }
}
