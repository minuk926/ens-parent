package kr.xit.core.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 * description : Springdoc(swagger) 설정
 *               설정내용이 상이한 경우 동일한 파일로 재정의 하거나 상속받아 사용
 * packageName : kr.xit.core.spring.config
 * fileName    : SpringDocsConfig
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
@ConditionalOnProperty(value = "springdoc.swagger-ui.enabled", havingValue = "true", matchIfMissing = false)
@Configuration
public class SpringDocsApiConfig {
    @Bean
    public GroupedOpenApi authentification() {
        return GroupedOpenApi.builder()
            .group("1. Authentification Api")
            .pathsToMatch(
                "/api/v1/auth/**"
            )
            .build();
    }

    @Bean
    public GroupedOpenApi sample() {
        return GroupedOpenApi.builder()
            .group("9. Sample Api")
            .pathsToMatch(
                "/api/v1/sample/**"
                //"/cmm/main/**",
            )
            .build();
    }

}
