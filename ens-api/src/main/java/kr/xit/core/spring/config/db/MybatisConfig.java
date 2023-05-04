package kr.xit.core.spring.config.db;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;

import javax.sql.DataSource;

import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.DateTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import kr.xit.core.support.mybatis.CamelCaseLinkedMap;
import kr.xit.core.support.mybatis.CamelCaseMap;
import kr.xit.core.support.mybatis.ObjectTypeHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//FIXME :: 재설정이 필요한 경우 해당프로젝트에 동일한 파일로 재정의 하여 사용
/**
 * <pre>
 * description : Mybatis 설정 - FIXME:: @DependsOn(value = {"dataSource"}) loading
 *               - 조건 : @DependsOn(value = {"dataSource"})
 * packageName : kr.xit.core.spring.config.support
 * fileName    : MybatisConfig
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see DatasourceConfig
 * @see JpaConfig
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableTransactionManagement
@MapperScan(
        basePackages = {"egovframework.**.mapper", "kr.xit.**.mapper"},
        sqlSessionFactoryRef = MybatisConfig.SQL_SESSION
)
@DependsOn(value = {"dataSource"})
public class MybatisConfig {
    private final DataSource dataSource;

    @Value("${app.mapper.resources}")
    private String MAPPER_RESOURCES;

    static final String MYBATIS_CONFIG_FILE = "classpath:/egovframework/mapper/config/mapper-config.xml";
    static final String SQL_SESSION = "sqlSession";

    @ConditionalOnMissingBean
    @Bean
    @Lazy
    public DefaultLobHandler lobHandler() {
        return new DefaultLobHandler();
    }

    @Bean(name = SQL_SESSION)
    public SqlSessionFactory sqlSession() throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        //sessionFactory.setConfiguration(mybatisConfiguration());
        sessionFactory.setConfigLocation(resolver.getResource(MYBATIS_CONFIG_FILE));
        //sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        //sessionFactory.setMapperLocations(resolver.getResources("classpath:/egovframework/mapper/**/*_" + dbType + ".xml"));
        sessionFactory.setMapperLocations(resolver.getResources(MAPPER_RESOURCES));
        return sessionFactory.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier(SQL_SESSION) SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Primary
    @Bean
    public PlatformTransactionManager mybatisTransactionManager() {
        DataSourceTransactionManager dstm = new DataSourceTransactionManager(dataSource);
        dstm.setGlobalRollbackOnParticipationFailure(false);
        return dstm;
    }

/*
    // -------------------------------------------------------------
    // TransactionAdvice 설정
    // -------------------------------------------------------------
    @Bean
    public TransactionInterceptor txAdvice(PlatformTransactionManager mybatisTransactionManager) {
        TransactionInterceptor txAdvice = new TransactionInterceptor();
        txAdvice.setTransactionManager(mybatisTransactionManager);
        txAdvice.setTransactionAttributeSource(getNameMatchTransactionAttributeSource());
        return txAdvice;
    }

    // -------------------------------------------------------------
    // TransactionAdvisor 설정
    // -------------------------------------------------------------
    @Bean
    public Advisor txAdvisor(PlatformTransactionManager mybatisTransactionManager) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(
            "execution(* egovframework.let..impl.*Impl.*(..)) or execution(* egovframework.com..*Impl.*(..))");
        return new DefaultPointcutAdvisor(pointcut, txAdvice(mybatisTransactionManager));
    }

    private NameMatchTransactionAttributeSource getNameMatchTransactionAttributeSource() {
        NameMatchTransactionAttributeSource txAttributeSource = new NameMatchTransactionAttributeSource();
        txAttributeSource.setNameMap(getRuleBasedTxAttributeMap());
        return txAttributeSource;
    }

    private HashMap<String, TransactionAttribute> getRuleBasedTxAttributeMap() {
        HashMap<String, TransactionAttribute> txMethods = new HashMap<String, TransactionAttribute>();

        RuleBasedTransactionAttribute txAttribute = new RuleBasedTransactionAttribute();
        txAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        txAttribute.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        txMethods.put("*", txAttribute);

        return txMethods;
    }
*/

    private org.apache.ibatis.session.Configuration mybatisConfiguration() {
        org.apache.ibatis.session.Configuration conf = new org.apache.ibatis.session.Configuration();
        conf.setCacheEnabled(true);
        conf.setAggressiveLazyLoading(false);
        conf.setMultipleResultSetsEnabled(true);
        conf.setUseColumnLabel(true);
        conf.setUseGeneratedKeys(false);
        conf.setAutoMappingBehavior(AutoMappingBehavior.PARTIAL);   // NONE / PARTIAL / FULL
        conf.setDefaultExecutorType(ExecutorType.REUSE);            // SIMPLE / REUSE / BATCH
        conf.setDefaultStatementTimeout(25);
        conf.setSafeRowBoundsEnabled(false);
        conf.setMapUnderscoreToCamelCase(true);
        conf.setLocalCacheScope(LocalCacheScope.SESSION);           // SESSION / STATEMENT
        conf.setJdbcTypeForNull(JdbcType.OTHER);                    // NULL / VARCHAR / OTHER 파라메터에 null 값이 있는경우 처리
        conf.setLazyLoadTriggerMethods(new HashSet<>(Arrays.asList("equals", "clone", "hashCode", "toString")));
        conf.setCallSettersOnNulls(true);                           // 조회시 null값 필드 set
        conf.setAggressiveLazyLoading(true);
        conf.setReturnInstanceForEmptyRow(true);


        conf.getTypeAliasRegistry().registerAliases("camelCaseMap", CamelCaseMap.class);
        conf.getTypeAliasRegistry().registerAliases("camelCaseLinkedMap", CamelCaseLinkedMap.class);

        conf.getTypeHandlerRegistry().register(Timestamp.class, DateTypeHandler.class);
        conf.getTypeHandlerRegistry().register(Time.class, DateTypeHandler.class);
        conf.getTypeHandlerRegistry().register(Date.class, DateTypeHandler.class);
        conf.getTypeHandlerRegistry().register(Object.class, ObjectTypeHandler.class);

        // TODO plugins 등록
/*
        <plugins>
		<plugin interceptor="kr.or.kuaa.core.support.mybatis.paging.OffsetLimitInterceptor">
			<property name="dialectClass" value="kr.or.kuaa.core.support.mybatis.paging.dialect.MySQLDialect"/>
		</plugin>
	</plugins>
*/
        return conf;
    }
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
