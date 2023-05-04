package kr.xit.core.spring.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.xit.core.consts.Constants;
import kr.xit.core.spring.config.auth.AuthentificationInterceptor;
import kr.xit.core.spring.config.properties.CorsProperties;
import kr.xit.core.spring.filter.SimpleCORSFilter;
import kr.xit.core.spring.interceptor.RestTemplateLoggingRequestInterceptor;
import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * description : Spring MVC 설정
 *               - filter, interceptor
 * packageName : kr.xit.core.spring.config
 * fileName    : WebMvcConfig
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * CommonsRequestLoggingFiler 등록
     * @return
     */
    @ConditionalOnProperty(value = "app.param.log.type", havingValue = "PAYLOAD", matchIfMissing = false)
    @Bean
    public FilterRegistrationBean requestLoggingFilter() {
        //CommonsRequestLoggingFilter loggingFilter = new CustomRequestLoggingFilter();
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeHeaders(false);
        loggingFilter.setBeforeMessagePrefix("\n//========================== Request(Before) ================================\n");
        loggingFilter.setBeforeMessageSuffix("\n//===========================================================================");

        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setMaxPayloadLength(1024* 1024);
        loggingFilter.setAfterMessagePrefix("\n//=========================== Request(After) ================================\n");
        loggingFilter.setAfterMessageSuffix("\n//===========================================================================");

        FilterRegistrationBean bean = new FilterRegistrationBean(loggingFilter);
        bean.setOrder(Ordered.LOWEST_PRECEDENCE);
        bean.addUrlPatterns(Constants.API_URL_PATTERNS);
        return bean;
    }

    /**
     * CORS Filter 등록
     * @return
     */
    @Bean
    public FilterRegistrationBean simpleCorsFilter() {
        SimpleCORSFilter corsFilter = new SimpleCORSFilter();
        FilterRegistrationBean bean = new FilterRegistrationBean(corsFilter);
        bean.setOrder(Ordered.LOWEST_PRECEDENCE);
        bean.addUrlPatterns(Constants.API_URL_PATTERNS);
        return bean;
    }

       /**
        * Restful logging
        *
        * @param restTemplateBuilder RestTemplateBuilder
        * @return RestTemplate
        * @see RestTemplateLoggingRequestInterceptor
        */
       @Bean
       @Qualifier("restTemplate")
       public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
           return restTemplateBuilder
                   // 로깅 인터셉터에서 Stream을 소비하므로 BufferingClientHttpRequestFactory 을 꼭 써야한다.
                   .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                   .setReadTimeout(Duration.ofSeconds(Constants.READ_TIMEOUT))
                   .setConnectTimeout(Duration.ofSeconds(Constants.CONNECT_TIMEOUT))
                   // UTF-8 인코딩으로 메시지 컨버터 추가
                   .messageConverters(new StringHttpMessageConverter(Constants.CHARSET))
                   // 로깅 인터셉터 설정
                   .interceptors(new RestTemplateLoggingRequestInterceptor())
                   // 로깅 인터셉터에서 Stream을 소비하므로 BufferingClientHttpRequestFactory 을 꼭 써야한다.
                   .customizers(restTemplate -> restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory())))
                   .build();
       }

    // @Bean
    // public FilterRegistrationBean readableRequestWrapperFilter() {
    //     ReadableRequestWrapperFilter readableRequestWrapperFilter = new ReadableRequestWrapperFilter();
    //
    //     FilterRegistrationBean bean = new FilterRegistrationBean(readableRequestWrapperFilter);
    //     bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    //     bean.addUrlPatterns(Constants.API_URL_PATTERNS);
    //     return bean;
    // }

    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(new AuthentificationInterceptor())
    		.addPathPatterns("/**/*")
    		.excludePathPatterns(
    			"/api/v1/auth/*"
//                "/api/v1/kakaopay/*"
    		);
    }

    // -------------------------------------------------------------
    // RequestMappingHandlerMapping 설정 View Controller 추가
    // -------------------------------------------------------------
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/cmmn/validator.do")
            .setViewName("cmmn/validator");
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}
