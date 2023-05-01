package kr.xit.core.spring.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;

/**
 * <pre>
 * description : JPA 설정에서 사용할 환경변수 설정
 *               재정의 필요시 상속하여 재정의 할것
 * packageName : kr.xit.core.spring.config.properties
 * fileName    : JpaConfigProperties
 * author      : xitdev
 * date        : 2023-05-01
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-05-01    xitdev       최초 생성
 *
 * </pre>
 */
public class JpaConfigProperties {
    public static final String ENTITY_PACKAGES = "{\"kr.xit.**.entity\"}";
    public static final String ENTITY_MANAGER_FACTORY = "entityManagerFactory";
    public static final String TRANSACTION_MANAGER = "transactionManager";
}
