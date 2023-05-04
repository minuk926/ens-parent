package kr.xit.core.spring.config.db;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//FIXME :: 재설정이 필요한 경우 해당 프로젝트에 동일한 파일로 재정의 하여 사용
/**
 * <pre>
 * description : JPA 설정 - FIXME:: app.jpa.enabled: true 설정이 있는 경우만 loading
 *               - 조건 : app.jpa.enabled: true (application-app.properties)
 * packageName : kr.xit.core.spring.config.support
 * fileName    : JpaConfig
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see DatasourceConfig
 * @see MybatisConfig
 */
@ConditionalOnProperty(value = "app.jpa.enabled", havingValue = "true", matchIfMissing = false)
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
    basePackages = JpaConfig.ENTITY_PACKAGES,
    entityManagerFactoryRef = JpaConfig.ENTITY_MANAGER_FACTORY,
    transactionManagerRef = JpaConfig.TRANSACTION_MANAGER
)
public class JpaConfig {
    @Value("${spring.jpa.properties.hibernate.hbm2ddl.auto}")
    String hbm2ddlAuto;

    private final DataSource dataSource;
    static final String ENTITY_PACKAGES = "kr.xit.**.entity";
    //static final String ENTITY_PACKAGES = "{\"kr.xit.**.entity\"}";
    static final String ENTITY_MANAGER_FACTORY = "entityManagerFactory";
    static final String TRANSACTION_MANAGER = "transactionManager";

    @ConditionalOnMissingBean
    @Bean
    @Lazy
    public DefaultLobHandler lobHandler() {
        return new DefaultLobHandler();
    }
    @ConfigurationProperties(prefix = "spring.jpa")
    @Bean
    public JpaProperties jpaProperties() {
        return new JpaProperties();
    }

    //@Primary
    @Bean(name = ENTITY_MANAGER_FACTORY)
    public EntityManagerFactory entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabasePlatform(jpaProperties().getDatabasePlatform());
        vendorAdapter.setDatabase(jpaProperties().getDatabase());
        vendorAdapter.setShowSql(jpaProperties().isShowSql());

        HibernateProperties hibernateProperties = new HibernateProperties();
        hibernateProperties.setDdlAuto(this.hbm2ddlAuto);
        Map<String, Object> propMap = hibernateProperties.determineHibernateProperties(
                jpaProperties().getProperties()
                , new HibernateSettings());

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan(ENTITY_PACKAGES);
        emf.setPersistenceUnitName("default");

        emf.setJpaVendorAdapter(vendorAdapter);
        emf.setJpaPropertyMap(propMap); //jpaProperties().getProperties());
        emf.afterPropertiesSet();
        //emf.getJpaPropertyMap().put(AvailableSettings.BEAN_CONTAINER, new SpringBeanContainer(entityManagerFactory))

        return emf.getObject();
    }

    //@Primary
    @Bean(name = TRANSACTION_MANAGER)
    public PlatformTransactionManager jpaTransactionManager(@Qualifier(ENTITY_MANAGER_FACTORY) EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        transactionManager.setNestedTransactionAllowed(true);
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
    /////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////
    // ChainedTransactionManager : trsnsaction binding
    ///////////////////////////////////////////////////////////////////////////////////////////
//    /**
//     * jap & mybatis Transaction binding
//     * @param entityManagerFactory LocalContainerEntityManagerFactoryBean
//     * @return PlatformTransactionManager
//     * @throws Exception Exception
//     */
//    @Bean
//    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) throws Exception {
//
//        // JPA transactionManager
//        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
//        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
//        jpaTransactionManager.setNestedTransactionAllowed(true);
//
//        // MYBATIS transactionManager
//        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
//        dataSourceTransactionManager.setDataSource(dataSource());
//        dataSourceTransactionManager.setGlobalRollbackOnParticipationFailure(false);
//
//        // creates chained transaction manager
//        return new ChainedTransactionManager(jpaTransactionManager, dataSourceTransactionManager);
//    }
    /////////////////////////////////////////////////////////////////////////////////////
}
