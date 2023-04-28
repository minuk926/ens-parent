package kr.xit.core.spring.config;

import com.p6spy.engine.spy.P6SpyOptions;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import kr.xit.core.spring.config.support.P6spySqlFormatConfiguration;

/**
 * <pre>
 * description : SQL log 출력(p6spy) - app.sql.logging.enabled = true 인 경우
 *               - JPA 하이버네이트 SQL 포함
 * packageName : kr.xit.core.spring.config
 * fileName    : P6spyLoggingConfig
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
@ConditionalOnProperty(value = "app.sql.logging.enabled", havingValue = "true", matchIfMissing = false)
@Configuration
public class P6spyLoggingConfig {

    /**
     * JPA SQL Logging
     * decorator.datasource.p6spy.enable-logging: true / false
     */
    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(P6spySqlFormatConfiguration.class.getName());
    }
}
