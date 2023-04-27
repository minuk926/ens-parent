package kr.xit.core.spring.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.MessageSourceAccessor;

import kr.xit.core.oauth.jwt.JwtTokenProvider;
import kr.xit.core.spring.config.support.ApplicationContextProvider;

/**
 * Get Bean Object
 * Filter / Interceptor 등에서 Bean 사용시 필요
 * (Bean으로 등록되는 클래스 내에서만 @Autowired / @Resource 등이 동작)
 *
 * @see ApplicationContextProvider
 */
public class SpringUtils {

	public static ApplicationContext getApplicationContext() {
		return ApplicationContextProvider.getApplicationContext();
	}
	
	public static boolean containsBean(String beanName) {
		return getApplicationContext().containsBean(beanName);
	}
	
	public static Object getBean(String beanName) {
		return getApplicationContext().getBean(beanName);
	}
	
	public static Object getBean(Class<?> clazz) {
		return getApplicationContext().getBean(clazz);
	}

	/**
	 *
	 * @return MessageSourceAccessor
	 */
	public static MessageSourceAccessor getMessageSourceAccessor(){
		return (MessageSourceAccessor)getBean(MessageSourceAccessor.class);
	}
	
//	public static CacheService getCodeService(){
//		return (CacheService)getBean(CacheService.class);
//	}


	/**
	 *
	 * @return AuthTokenProvider
	 */
	public static JwtTokenProvider getJwtTokenProvider(){
		return (JwtTokenProvider)getBean(JwtTokenProvider.class);
	}
}
