package kr.xit.core.spring.config.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * ApplicationContext 를 이용하여 component 주입
 * Bean으로 등록되는 클래스 내에서만 @Autowired / @Resource 등이 동작
 * Filter / Interceptor 등에서 Bean 사용시 필요
 *
 * @see com.xit.core.util.SpringUtils
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    
    private static ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        applicationContext = ctx;
    }
    
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
