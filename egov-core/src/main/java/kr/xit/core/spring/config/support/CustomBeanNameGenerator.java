package kr.xit.core.spring.config.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.lang.NonNull;

/**
 * Bean 이름 식별시 패키지를 포함하도록 지정
 * /v1/api, /v2/api 형태로 생성 가능하도록
 */
/**
 * <pre>
 * description : Bean 이름 식별시 패키지를 포함하도록 지정
 *              - /v1/api, /v2/api 형태로 생성 가능
 * packageName : kr.xit.core.spring.config.support
 * fileName    : CustomBeanNameGenerator
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
public class CustomBeanNameGenerator implements BeanNameGenerator {

    /**
     * basePackages 외에 scaning된 beanGenerator
     */
    private static final BeanNameGenerator DELEGATE = new AnnotationBeanNameGenerator();

    /**
     * VersioningBeanNameGenerator 대상 package 경로
     */
    private final List<String> basePackages = new ArrayList<>();

    @Override
    public @NonNull String generateBeanName(@NonNull BeanDefinition definition, @NonNull BeanDefinitionRegistry registry) {
        if(isTargetPackageBean(definition)) {
            return getBeanName(definition);
        }

        return DELEGATE.generateBeanName(definition, registry);
    }

    private boolean isTargetPackageBean(BeanDefinition definition) {
        String beanClassName = getBeanName(definition);
        return basePackages.stream().anyMatch(beanClassName::startsWith);
    }

    private String getBeanName(BeanDefinition definition) {
        return definition.getBeanClassName();
    }

    public void addBasePackages(List<String> basePackages) {
        this.basePackages.addAll(basePackages);
    }
}
