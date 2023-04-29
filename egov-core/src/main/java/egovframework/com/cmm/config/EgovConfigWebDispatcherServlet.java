package egovframework.com.cmm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName : EgovConfigWebDispatcherServlet.java
 * @Description : DispatcherServlet 설정
 *
 * @author : 윤주호
 * @since  : 2021. 7. 20
 * @version : 1.0
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일              수정자               수정내용
 *  -------------  ------------   ---------------------
 *   2021. 7. 20    윤주호               최초 생성
 * </pre>
 *
 */
@Configuration
// @ComponentScan(
// 	basePackages = {"egovframework", "kr.xit"},
// 	excludeFilters = {
// 		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Service.class),
// 		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Repository.class),
// 		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class)
// 	}
// )
public class EgovConfigWebDispatcherServlet implements WebMvcConfigurer {

	//final static String RESOLVER_DEFAULT_ERROR_VIEW = "cmm/error/egovError";

	//final static int URL_BASED_VIEW_RESOLVER_ORDER = 1;
	//final static String URL_BASED_VIEW_RESOLVER_PREFIX = "/WEB-INF/jsp/";
	//final static String URL_BASED_VIEW_RESOLVER_SUFFIX = ".jsp";

	//private final String[] CORS_ORIGIN_SERVER_URLS = {"http://127.0.0.1:3000", "http://localhost:3000"};

	// =====================================================================
	// RequestMappingHandlerMapping 설정
	// =====================================================================
	// -------------------------------------------------------------
	// RequestMappingHandlerMapping 설정 - Interceptor 추가
	// -------------------------------------------------------------
// 	@Override
// 	public void addInterceptors(InterceptorRegistry registry) {
// 		registry.addInterceptor(new AuthenticInterceptor())
// 			.addPathPatterns(
// //				"/cop/com/*.do",
// //				"/cop/bbs/*Master*.do",
// 				"/uat/uia/*.do")
// 			.excludePathPatterns(
// 				"/api/v1/auth/*.do",
// 				"/uat/uia/egovLoginUsr.do",
// 				"/uat/uia/egovLoginUsrAPI.do"
// 				);
// 		registry.addInterceptor(new CustomAuthenticInterceptor())
// 			.addPathPatterns(
// 				"/**/*.do")
// 			.excludePathPatterns(
// 				"/api/v1/auth/*.do");
// 	}

	// -------------------------------------------------------------
	// RequestMappingHandlerMapping 설정 View Controller 추가
	// -------------------------------------------------------------
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/cmmn/validator.do")
			.setViewName("cmmn/validator");
		registry.addViewController("/").setViewName("forward:/index.html");
	}

	// -------------------------------------------------------------
	// HandlerExceptionResolver 설정
	// -------------------------------------------------------------
	/*
	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		SimpleMappingExceptionResolver simpleMappingExceptionResolver = new SimpleMappingExceptionResolver();

		simpleMappingExceptionResolver.setDefaultErrorView(RESOLVER_DEFAULT_ERROR_VIEW);

		Properties mappings = new Properties();
		mappings.setProperty("org.springframework.dao.DataAccessException", "cmm/error/dataAccessFailure");
		mappings.setProperty("org.springframework.transaction.TransactionException", "cmm/error/transactionFailure");
		mappings.setProperty("org.egovframe.rte.fdl.cmmn.exception.EgovBizException", "cmm/error/egovError");
		mappings.setProperty("org.springframework.security.AccessDeniedException", "cmm/error/accessDenied");

		simpleMappingExceptionResolver.setExceptionMappings(mappings);

		exceptionResolvers.add(simpleMappingExceptionResolver);
	}
	
	*/
	// -------------------------------------------------------------
	// View Resolver 설정
	// -------------------------------------------------------------
	/*
	 * @Bean public UrlBasedViewResolver urlBasedViewResolver() {
	 * UrlBasedViewResolver urlBasedViewResolver = new UrlBasedViewResolver();
	 * urlBasedViewResolver.setOrder(URL_BASED_VIEW_RESOLVER_ORDER);
	 * urlBasedViewResolver.setViewClass(JstlView.class);
	 * urlBasedViewResolver.setPrefix(URL_BASED_VIEW_RESOLVER_PREFIX);
	 * urlBasedViewResolver.setSuffix(URL_BASED_VIEW_RESOLVER_SUFFIX); return
	 * urlBasedViewResolver; }
	 */

	// -------------------------------------------------------------
	// CORS 설정 추가
	// -------------------------------------------------------------
//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("*.do").allowedOrigins(CORS_ORIGIN_SERVER_URLS);
//	}



	// @Override
	// public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
	// 	resolvers.add(customArgumentResolver());
	// 	resolvers.add(pageableArgumentResolver());
	// }
	//
	// @Bean
	// public CustomArgumentResolver customArgumentResolver(){
	// 	return new CustomArgumentResolver();
	// }
	//
	// @Bean
	// public PageableArgumentResolver pageableArgumentResolver(){
	// 	return new PageableArgumentResolver();
	// }
}