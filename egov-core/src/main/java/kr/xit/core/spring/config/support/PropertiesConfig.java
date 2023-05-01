package kr.xit.core.spring.config.support;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * description :
 *
 * packageName : kr.xit.core.spring.config.support
 * fileName    : PropertiesConfig
 * author      : xitdev
 * date        : 2023-05-01
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-05-01    xitdev       최초 생성
 *
 * </pre>
 */
@Slf4j
@Configuration
public class PropertiesConfig {

    //private static final String SERVER_FILE = "file:/data/dynamic";
    private static final String CLASSPATH_FILE = "classpath:config/application-app";
    private static final String SUFFIX = ".yml";

    // @Bean
    // ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource(){
    //     ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    //     //messageSource.setBasenames(CLASSPATH_FILE, SERVER_FILE);
    //     messageSource.setBasenames(CLASSPATH_FILE);
    //     messageSource.setCacheMillis(5000);
    //
    //     return messageSource;
    // }
    @Bean
    PropertiesConfiguration propertiesConfiguration() throws Exception {
        PropertiesConfiguration configuration = null;
        try {
            configuration = new PropertiesConfiguration(CLASSPATH_FILE + SUFFIX);
        } catch (ConfigurationException e) {
            log.error(e.getMessage());
            //configuration = new PropertiesConfiguration(CLASSPATH_FILE + SUFFIX);
        }
        configuration.setReloadingStrategy(new FileChangedReloadingStrategy());

        return configuration;
    }
}
