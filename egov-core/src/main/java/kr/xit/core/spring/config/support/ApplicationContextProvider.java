package kr.xit.core.spring.config.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * description : ApplicationContext 를 이용하여 component 주입
 *              - ApplicationContextAware 구현
 *              - Bean으로 등록되는 클래스 내에서만 @Autowired / @Resource 등이 동작
 *              - Filter / Interceptor 등에서 Bean 사용시 필요
 * packageName : kr.xit.core.spring.config.support
 * fileName    : ApplicationContextProvider
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see kr.xit.core.spring.util.SpringUtils
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
