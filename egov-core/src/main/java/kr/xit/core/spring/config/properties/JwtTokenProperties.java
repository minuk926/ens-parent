package kr.xit.core.spring.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * description : Java web token Properties
 *               - properties : app.token 항목에 정의
 * packageName : kr.xit.core.spring.config.properties
 * fileName    : JwtTokenProperties
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
@ConfigurationProperties(prefix = "app.token")
public class JwtTokenProperties {
    private String typ;
    private String grant;
    private String alg;
    private String issure;
    private String audience;
    // minute
    private int tokenExpiry;
    // day
    private int refreshTokenExpiry;
    private String saveType;
}
