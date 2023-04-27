package kr.xit.core.spring.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

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
