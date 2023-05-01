package kr.xit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

import kr.xit.core.spring.config.properties.CorsProperties;
import kr.xit.core.spring.config.properties.JwtTokenProperties;
import kr.xit.core.spring.config.support.CustomBeanNameGenerator;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * description : ens core application main
 * packageName : kr.xit
 * fileName    : EnsCoreBootApplication
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
@Slf4j
@SpringBootApplication
@EnableConfigurationProperties({
	CorsProperties.class,
	JwtTokenProperties.class
})
@ServletComponentScan
//@Import({EgovWebApplicationInitializer.class})
@ComponentScan(
	nameGenerator = CustomBeanNameGenerator.class,
	basePackages = {"egovframework.com", "kr.xit"}
)
public class EnsApiBootApplication {
	static final List<String> basePackages = new ArrayList<>(
		Arrays.asList("egovframework", "kr.xit")
	);

	public static void main(String[] args) {
		log.info("====================================================================");
		log.info("==== EnsApiBootApplication Application start :: active profiles - {} ===", System.getProperty("spring.profiles.active"));
		if(Objects.isNull(System.getProperty("spring.profiles.active"))) {

			log.error(">>>>>>>>>>>>>>        Undefined start VM option       <<<<<<<<<<<<<<");
			log.error(">>>>>>>>>>>>>> -Dspring.profiles.active=local|dev|prd <<<<<<<<<<<<<<");
			log.error("============== EnsApiBootApplication Application start fail ===============");
			log.error("====================================================================");
			System.exit(-1);
		}
		log.info("====================================================================");

		// SpringApplication springApplication = new SpringApplication(EgovBootApplication.class);
		// springApplication.setBannerMode(Banner.Mode.OFF);
		// //springApplication.setLogStartupInfo(false);
		// springApplication.run(args);

		// beanName Generator 등록 : API v1, v2 등으로 분류하는 경우
		// Bean 이름 식별시 풀패키지 명으로 식별 하도록 함
		CustomBeanNameGenerator beanNameGenerator = new CustomBeanNameGenerator();
		beanNameGenerator.addBasePackages(basePackages);

		SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder(EnsApiBootApplication.class);
		applicationBuilder.beanNameGenerator(beanNameGenerator);
		applicationBuilder.application().setBannerMode(Banner.Mode.OFF);
		applicationBuilder.application().setLogStartupInfo(false);
		//TODO : 이벤트 실행 시점이 Application context 실행 이전인 경우 리스너 추가
		//application.listeners(new xitCoreApplicationListner());
		applicationBuilder.build()
			.run(args);

		log.info("=========================================================================================");
		log.info("========== EnsApiBootApplication Application load Complete :: active profiles - {} ==========", System.getProperty("spring.profiles.active"));
		log.info("=========================================================================================");
	}

}
