package kr.xit.ens.support.kakao.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <pre>
 * description :
 *
 * packageName : kr.xit.ens.support.kakaopay.dto
 * fileName    : KakaotalkDTO
 * author      : xitdev
 * date        : 2023-05-03
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-05-03    xitdev       최초 생성
 *
 * </pre>
 */
public class KkoPayEltrDocDTO {

    @Schema(name = "RequestSendReq", description = "문서발송 요청 DTO")
    @Data
    @NoArgsConstructor
    @SuperBuilder
    //@AllArgsConstructor
    public static class RequestSendReq {
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @Valid
        private RequestSend document;
    }

    @Schema(name = "RequestSend", description = "문서발송 요청 파라메터 DTO")
    @Data
    @NoArgsConstructor
    @SuperBuilder
    //@AllArgsConstructor
    public static class RequestSend {
        /**
         * 발송할 문서의 제목 : 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, title = "발송할 문서의 제목", example = "문서 제목")
        @Size(min = 1, max = 40, message = "발송할 문서 제목(1 ~ 40자)")
        private String title;

        /**
         * 받는이에 대한 정보 - 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @Valid
        private Receiver receiver;

        /**
         * 문서 속성 정보 - 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @Valid
        private Property property;



        /**
         * 처리마감시간(절대시간) - 카카오톡 메시지를 수신한 사용자가 전자문서를 열람을 할 수 있는 시간
         * read_expired_sec 미전송시 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "처리마감시간(절대시간)", example = "1617202800")
        @Digits(integer = 10, fraction = 0, message = "처리마감시간(절대시간:max=10자리)")
        private long read_expired_at;

        /**
         * 처리마감시간(상대시간) - 권장값: 30일 (2592000 sec)
         * read_expired_at 미전송시 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "처리마감시간(상대시간)", example = " ")
        @Digits(integer = 8, fraction = 0, message = "처리마감시간(절대시간:max=8자리)")
        private int read_expired_sec;



        /**
         * 문서 원문(열람정보)에 대한 hash 값 - 공인전자문서 유통정보 등록 시 필수
         */
        @Schema(title = "문서 원문(열람정보)에 대한 hash 값", example = "6EFE827AC88914DE471C621AE")
        @Size(max = 40, message = "전송일자(max=40자)")
        private String hash;







        /**
         * 문서의 메타정보 - 향후 문서 검색을 위해서 활용될 메타 정보(현재 미 제공)
         */
        @Schema(title = "문서의 메타정보(현재 미 제공)", example = "[\"NOTICE\"]")
        private List<String> common_categories;





        // /**
        //  * 문서 안내 추가 정보
        //  * 해당 파라미터는 알림톡 미발송 상품의 경우에만 사용이 가능합니다
        //  */
        // @Schema(title = "문서 안내 추가 정보", example = " ")
        // private Integer bridge;
    }


    @Schema(name = "Receiver", description = "RequestSend(문서발송 요청 파라메터)의 receiver(받는이)에 대한 정보 DTO")
    @Data
    @NoArgsConstructor
    @SuperBuilder
    //@AllArgsConstructor
    public static class Receiver {
        /**
         * 받는이 CI
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "받는이 CI", example = " ")
        @Size(max = 88, message = "받는이 CI(max=88자)")
        private String ci;

        /**
         * 받는이 전화번호
         * ci 미전송시 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "받는이 전화번호", example = "01012345678")
        @Size(max = 11, message = "받는이 전화번호(max=11자)")
        private String phone_number;

        /**
         * 받는 이 이름
         * ci 미전송시 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, title = "받는 이 이름", example = "김페이")
        @Size(max = 20, message = "받는이 이름(max=20자)")
        private String name;

        /**
         * 받는 이 생년월일 (YYYYMMDD 형식)
         * ci 미전송시 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "받는 이 생년월일 (YYYYMMDD 형식)", example = "19801101")
        @Size(max = 8, message = "받는이 생년월일(YYYYMMDD:max=8자)")
        private String birthday;

        /**
         * 성명 검증 옵션
         * CI 전송 시 생략 가능
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "성명 검증 옵션", example = "false")
        private Boolean is_required_verify_name;
    }

    @Schema(name = "Property", description = "RequestSend(문서발송 요청 파라메터)의 property(문서속성)에 대한 정보 DTO")
    @Data
    @SuperBuilder
    @NoArgsConstructor
    //@AllArgsConstructor
    public static class Property {
        /**
         * 본인인증 후 사용자에게 보여줄 웹페이지 주소 : 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, title = "본인인증 후 사용자에게 보여줄 웹페이지 주소", example = "2592000")
        //@NotBlank
        @Size(max = 100, message = "본인인증 후 사용자에게 보여줄 웹페이지 주소(max=100자)")
        private String link;

        /**
         * 고객센터 전화번호 : 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, title = "고객센터 전화번호", example = "02-123-4567")
        @Size(max = 20, message = "고객센터 전화번호(max=20자)")
        private String cs_number;

        /**
         * 고객센터 전화번호 : 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, title = "고객센터 명", example = "콜센터")
        //@NotBlank
        @Size(max = 10, message = "고객센터 명(max=10자)")
        private String cs_name;

        /**
         * 이용기관에서 해당 값을 다시 받고자 할 내용의 값
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "이용기관에서 해당 값을 다시 받고자 할 내용의 값", example = "payload 파라미터 입니다.")
        @Size(max = 200, message = "이용기관에서 해당 값을 다시 받고자 할 내용의 값(max=200자)")
        private String payload;

        /**
         * 메세지 - 사용자에게 전송하는 문서에 대한 설명
         * 노출위치 : 문서수신 메시지(알림톡)가 도착했음을 알리는 카카오톡 메시지 내부
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "사용자에게 전송하는 문서에 대한 설명", example = "해당 안내문은 다음과 같습니다.")
        @Size(max = 500, message = "메세지(max=500자)")
        private String message;
    }

    // @Schema(name = "KkoAlimtalkAcceptReqDTO")
    // @SuperBuilder
    // @Getter
    // @NoArgsConstructor
    // @AllArgsConstructor
    // public static class EnsAcceptReqDTO {
    //
    //     @Length(max = 30, message = "중개사업자의 최대 길이를 초과 했습니다.")
    //     @Schema(required = false, title = "중개사업자", example = "dozn")
    //     private String vender;
    //
    //     @Length(max = 20, message = "기관코드의 최대 길이를 초과 했습니다.")
    //     @NotEmpty(message = "기관코드는 필수 입력값 입니다.")
    //     @Schema(required = true, title = "기관코드", example = "EX_ORG001")
    //     private String org_cd;
    //
    //     @Length(max = 30, message = "템플릿코드의 최대 길이를 초과 했습니다.")
    //     @NotEmpty(message = "템플릿코드는 필수 입력값 입니다.")
    //     @Schema(required = false, title = "템플릿코드", example = "EX_TMPLT001")
    //     private String tmplt_cd;
    //
    //     @Schema(required = false, title = "제목", example = " ")
    //     private String post_bundle_title;
    //
    //     @NotEmpty(message = "전송일시는 필수 입력값 입니다")
    //     @Schema(required = true, title = "접수일시(yyyyMMddHHmmss)", example = "20220317192730")
    //     private String send_dt;
    //
    //     @NotEmpty(message = "마감일시는 필수 입력값 입니다")
    //     @Schema(required = true, title = "마감일시(yyyyMMddHHmmss)", example = "20220317235959")
    //     private String close_dt;
    //
    //     //    @Valid
    //     //    @NotEmpty(message = "문서목록은 필수 입력값 입니다")
    //     //    private List<T> documents;
    //
    // }
    //
    // @SuperBuilder
    // @Getter
    // @NoArgsConstructor
    // @AllArgsConstructor
    // public static class EnsRsltRespDTO {
    //     //@Schema(required = true, title = "상태정보", example = " ")
    //     //@Enumerated(EnumType.STRING)
    //     //private StatCd statCd;
    //     @Schema(required = true, title = "기관코드", example = "EX_ORG001")
    //     private String orgCd;
    //     @Schema(required = false, title = "템플릿코드", example = "EX_TMPLT001")
    //     private String tmpltCd;
    //     @Schema(required = false, title = "제목", example = " ")
    //     private String postBundleTitle;
    //     @Schema(required = true, title = "접수일시(yyyyMMddHHmmss)", example = "20220317192730")
    //     private String sendDt;
    //     @Schema(required = true, title = "마감일시(yyyyMMddHHmmss)", example = "20220317235959")
    //     private String closeDt;
    //     @Schema(required = true, title = "마감여부", example = "false")
    //     private Boolean closeAt;
    // }
}
