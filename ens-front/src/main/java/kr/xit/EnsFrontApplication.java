package kr.xit;

import java.util.Objects;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * description : ens Front application main
 *               ServletComponentScan
 *               - 서블릿컴포넌트(필터, 서블릿, 리스너)를 스캔해서 빈으로 등록
 *               - WebFilter, WebServlet, WebListener annotaion sacan
 *               - SpringBoot의 내장톰캣을 사용하는 경우에만 동작
 *               ConfigurationPropertiesScan
 *               - ConfigurationProperties annotaion class scan 등록
 *               - EnableConfigurationProperties 대체
 * packageName : kr.xit
 * fileName    : EnsFrontApplication
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
//@ConfigurationPropertiesScan(basePackages = {"egovframework", "kr.xit"})
//@ServletComponentScan
@ComponentScan(
    basePackages = {"egovframework.com", "kr.xit"}
)
public class EnsFrontApplication {

    public static void main(String[] args) {
        log.info("====================================================================");
        log.info("====    EnsFrontApplication start :: active profiles - {}    ====", System.getProperty("spring.profiles.active"));
        if(Objects.isNull(System.getProperty("spring.profiles.active"))) {

            log.error(">>>>>>>>>>>>>>        Undefined start VM option       <<<<<<<<<<<<<<");
            log.error(">>>>>>>>>>>>>> -Dspring.profiles.active=local|dev|prd <<<<<<<<<<<<<<");
            log.error("============== EnsFrontApplication start fail ===============");
            log.error("====================================================================");
            System.exit(-1);
        }
        log.info("====================================================================");

        SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder(EnsFrontApplication.class);
        applicationBuilder.application().setBannerMode(Banner.Mode.OFF);
        applicationBuilder.application().setLogStartupInfo(false);
        //TODO : 이벤트 실행 시점이 Application context 실행 이전인 경우 리스너 추가
        //application.listeners(new xitCoreApplicationListner());
        applicationBuilder.build()
            .run(args);

        log.info("=========================================================================================");
        log.info("==========     EnsFrontApplication load Complete :: active profiles - {}    ==========", System.getProperty("spring.profiles.active"), "");
        log.info("=========================================================================================");
    }

}
