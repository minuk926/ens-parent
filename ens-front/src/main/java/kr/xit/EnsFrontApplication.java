package kr.xit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import kr.xit.core.spring.config.support.CustomBeanNameGenerator;

@SpringBootApplication
@ComponentScan(
    basePackages = {"egovframework.com", "kr.xit"}
)
public class EnsFrontApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnsFrontApplication.class, args);
    }

}
