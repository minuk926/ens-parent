package kr.xit.core.spring.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import egovframework.com.cmm.interceptor.AuthenticInterceptor;
import egovframework.com.cmm.interceptor.CustomAuthenticInterceptor;
import kr.xit.core.Constants;
import kr.xit.core.spring.config.auth.AuthentificationInterceptor;

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
    		.addPathPatterns("/**/*.do")
    		.excludePathPatterns(
    			"/api/v1/auth/*.do"
    		);
    }
}
