package kr.xit.core.spring.config;

import java.util.Arrays;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SpringDocsConfig {

    @Bean
    public OpenAPI openAPI(
        @Value("${springdoc.version}") String version,
        @Value("${app.url}") String url,
        @Value("${spring.profiles.active}") String active) {

        Info info = new Info()
            .title("전자고지 Rest API 문서")  // 타이틀
            .version(version)           // 문서 버전
            .description("잘못된 부분이나 오류 발생 시 바로 말씀해주세요.") // 문서 설명
            .contact(new Contact()      // 연락처
                .name("관리자")
                .email("admin@xit.co.kr")
                .url("http://www.xerotech.co.kr/"));

       // List<Server> servers = Arrays.asList(new Server().url(url).description(url + "(" + active + ")"));

        // Security 스키마 설정
        SecurityScheme securityScheme = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            // .name(HttpHeaders.AUTHORIZATION);
            .name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
            // Security 인증 컴포넌트 설정
            .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
            // API 마다 Security 인증 컴포넌트 설정
            //.addSecurityItem(new SecurityRequirement().addList("JWT"))
            .security(Arrays.asList(securityRequirement))
            .info(info) ;
           // .servers(servers);
    }

    @Bean
    public GroupedOpenApi egovFramework1() {
        return GroupedOpenApi.builder()
            .group("1-1. egov-framework Common")
            .pathsToMatch(
                "/cmm/fms/**",
                "/cmm/main/**",
                "/uat/**"
            )
            .build();
    }

    @Bean
    public GroupedOpenApi egovFramework2() {
        return GroupedOpenApi.builder()
            .group("1-2. egov-framework Etc")
            .pathsToMatch(
                "/cop/**",
                "/sym/**"
            )
            .build();
    }

    @Bean
    public GroupedOpenApi authentification() {
        return GroupedOpenApi.builder()
            .group("1-0. Authentification Api")
            .pathsToMatch(
                "/api/v1/auth/**"
            )
            .build();
    }

    @Bean
    public GroupedOpenApi xitEms() {
        return GroupedOpenApi.builder()
            .group("2. 전자고지")
            .pathsToMatch(
                "/api/v1/**"
            )
            .build();
    }
}
