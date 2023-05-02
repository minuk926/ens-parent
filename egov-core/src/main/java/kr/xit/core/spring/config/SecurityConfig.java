// package kr.xit.core.spring.config;
//
// import java.util.Arrays;
//
// import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.builders.WebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsUtils;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
// import kr.xit.core.spring.config.auth.jwt.JwtAuthenticationEntryPoint;
// import kr.xit.core.spring.config.auth.jwt.JwtAuthenticationFilter;
// import kr.xit.core.spring.config.auth.jwt.TokenAccessDeniedHandler;
// import kr.xit.core.spring.config.auth.jwt.JwtTokenProvider;
// import kr.xit.core.spring.config.properties.CorsProperties;
// import lombok.RequiredArgsConstructor;
//
// /**
//  * <pre>
//  * description : Spring Security 설정
//  * packageName : kr.xit.core.spring.config
//  * fileName    : SecurityConfig
//  * author      : julim
//  * date        : 2023-04-28
//  * ======================================================================
//  * 변경일         변경자        변경 내용
//  * ----------------------------------------------------------------------
//  * 2023-04-28    julim       최초 생성
//  *
//  * </pre>
//  */
// @RequiredArgsConstructor
// @Configuration
// public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//     private final CorsProperties corsProperties;
//     private final JwtTokenProvider jwtTokenProvider;
//     private final JwtAuthenticationEntryPoint unauthorizedHandler;
//     private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
//
//     private static final String[] AUTH_WHITELIST = {
//         "/favicon.ico",
//         "/static/**",
//         "/api-docs",
//         "/api-docs/**",
//         "/h2-console/**",
//         "/v2/api-docs/**",
//         "/swagger-ui.html",
//         "/swagger-ui/**",
//         "/swagger-resources/**",
//         "/webjars/**",
//         "favicon.ico",
//         "/configuration/ui",
//         "/configuration/security",
//         "/resources/**",
//         "/api/v1/auth/**",
//         "/api/v1/sample/**",
//         "/"
//     };
//
//     @Bean
//     public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
//         return new JwtAuthenticationFilter(jwtTokenProvider);
//     }
//
//     /**
//      * 1. configure( WebSecurity) : 서비스 전체에 영향을 미치는 설정
//      *   - web.ignoring를 통한 security skip(resource file),  debug 모드설정 등
//      * @param web WebSecurity
//      */
//     @Override
//     public void configure(WebSecurity web) {
//         web.ignoring()
//             .antMatchers(AUTH_WHITELIST);
//     }
//
//     @Override
//     protected void configure(HttpSecurity http) throws Exception {
//         http.cors().and()
//             .csrf().disable()
//             .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//             .authorizeRequests()
//             // GET, POST 요청시 : OPTIONS preflight 요청 - 실제 서버가 살아있는지를 사전에 확인하는 요청
//             // Spring에서 OPTIONS에 대한 요청을 막고 있어 OPTIONS 요청이 왔을 때도 오류를 리턴하지 않도록 설정
//             .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
//             .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//             .mvcMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//             .antMatchers(
//                 "/",
//                 "/favicon.ico",
//                 "/**/*.png",
//                 "/**/*.gif",
//                 "/**/*.svg",
//                 "/**/*.jpg",
//                 "/**/*.html",
//                 "/**/*.css",
//                 "/**/*.js").permitAll()
//             .antMatchers(AUTH_WHITELIST).permitAll()
//             .antMatchers("/**/h2-console/**", "/**/auth/**", "/**/users/**", "/**/biz/**").permitAll()
//             .anyRequest().permitAll() //authenticated()
//             .and()
//             .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
//             .accessDeniedHandler(tokenAccessDeniedHandler)
//             ;
//             //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//         http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//     }
//
//     /*
//      * Cors 설정
//      * */
//     @Bean
//     public UrlBasedCorsConfigurationSource corsConfigurationSource() {
//         UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();
//
//         CorsConfiguration corsConfig = new CorsConfiguration();
//         corsConfig.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
//         corsConfig.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
//         corsConfig.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
//         corsConfig.setAllowCredentials(true);
//         corsConfig.setMaxAge(corsConfig.getMaxAge());
//
//         corsConfigSource.registerCorsConfiguration("/**", corsConfig);
//         return corsConfigSource;
//     }
// }
