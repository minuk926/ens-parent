package kr.xit.core.spring.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * description : CORS Properties
 *               - properties : cors 항목에 정의
 * packageName : kr.xit.core.spring.config.properties
 * fileName    : CorsProperties
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
    /**
     * 헤더에 작성된 출처만 브라우저가 리소스를 접근할 수 있도록 허용함.
     * * 이면 모든 곳에 공개
     */
    private String allowedOrigins;
    /**
     * 리소스 접근을 허용하는 HTTP 메서드를 지정
     */
    private String allowedMethods;
    /**
     * 요청을 허용하는 해더
     */
    private String allowedHeaders;
    /**
     * 클라이언트에서 preflight 의 요청 결과를 저장할 기간을 지정
     * 해당 시간 동안 preflight 요청을 캐시하는 설정으로, 첫 요청 이후 60초 동안은 OPTIONS 메소드를 사용하는 예비 요청을 보내지 않는다.
     */
    private Long maxAge;
    /**
     * 클라이언트 요청이 쿠키를 통해서 자격 증명을 해야 하는 경우에 true.
     * 자바스크립트 요청에서 credentials가 include일 때 요청에 대한 응답을 할 수 있는지를 나타낸다.
     */
    private Boolean allowCredentials;
    /**
     * 기본적으로 브라우저에게 노출이 되지 않지만, 브라우저 측에서 접근할 수 있게 허용해주는 헤더를 지정
     * Content-Length
     */
    private String exposeHeader;
}
