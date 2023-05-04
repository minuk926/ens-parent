package kr.xit.ens.support.common;

/**
 * <pre>
 * description :
 *
 * packageName : kr.xit.ens.support.common
 * fileName    : KakaoConstants
 * author      : xitdev
 * date        : 2023-05-04
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-05-04    xitdev       최초 생성
 *
 * </pre>
 */
public class KakaoConstants {

    /**
     * 카카오페이 전자문서 요청 헤더 필드명
     */
    public enum HeaderName {
        TOKEN("Authorization"),	 //Token
        UUID("Contract-Uuid"),   //Contract-Uuid
        ;     // TOKEN

        private final String code;

        HeaderName(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }
    }
}
