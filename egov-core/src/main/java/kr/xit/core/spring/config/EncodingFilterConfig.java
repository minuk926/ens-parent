package kr.xit.core.spring.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.filter.OrderedCharacterEncodingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * <pre>
 * description : 기본 UTF-8인데, encoding이 EUC-KR로 들어오는 경우가 있는 경우(외부에서 들어오는 - 결재 등 -) 사용
 *               - 조건 : spring.http.encoding.force: false
 * packageName : kr.xit.core.spring.config
 * fileName    : EncodingFilterConfig
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
//FIXME :: 기본 UTF-8인데, encoding이 EUC-KR로 들어오는 경우가 있는 경우(외부에서 들어오는 - 결재 등 -) 사용
@ConditionalOnProperty(value = "spring.http.encoding.force", havingValue = "false", matchIfMissing = false)
@Configuration
public class EncodingFilterConfig {

    @Bean
    public FilterRegistrationBean encodingFilterBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
        filter.setForceEncoding(true);
        filter.setEncoding("MS949");
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/ms949filterUrl/*");
        return registrationBean;
    }
}
