package kr.xit.core.egov;// package kr.xit.core.config.egov;
//
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Import;
// import org.springframework.context.annotation.PropertySource;
// import org.springframework.context.annotation.PropertySources;
//
// @Configuration
// @Import({
// 	EgovConfigAppAspect.class,
// 	EgovConfigAppCommon.class,
// 	EgovConfigAppDatasource.class,
// 	EgovConfigAppIdGen.class,
// 	EgovConfigAppProperties.class,
// 	EgovConfigAppMapper.class,
// 	EgovConfigAppTransaction.class,
// 	EgovConfigAppValidator.class,
// 	EgovConfigAppWhitelist.class
// })
// @PropertySources({
// 	@PropertySource("classpath:/application-${spring.profiles.active}.yml")
// }) //CAUTION: min JDK 8
// public class EgovConfigApp {
//
// }