package kr.xit.core.spring.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.xit.core.spring.resolver.CustomArgumentResolver;
import kr.xit.core.spring.resolver.PageableArgumentResolver;

@Configuration
public class ArgumentResolverConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CustomArgumentResolver());
        resolvers.add(new PageableArgumentResolver());
    }
    //
    // @Bean
    // public CustomArgumentResolver customArgumentResolver(){
    //     return new CustomArgumentResolver();
    // }
    //
    // @Bean
    // public PageableArgumentResolver pageableArgumentResolver(){
    //     return new PageableArgumentResolver();
    // }
}
