package kr.xit.core.spring.config.db;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * description : 실제 필요한 경우만 커넥션을 점유하도록
 *               LazyConnectionDataSourceProxy 사용
 *               일반 Datasource 사용시
 *               - Spring은 트랜잭션에 진입시 데이타 소스의 커넥션을 get
 *               - ehcache, hibernate 영속성 컨택슽트 1차캐시 등에도 커넥션을 get
 *               - multi-datasource 에서 트랜잭션 진입 이후 datasource 분기 불가
 * packageName : kr.xit.core.spring.config.db
 * fileName    : DatasourceConfig
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see JpaConfig
 * @see MybatisConfig
 */
@Slf4j
@Configuration
public class DatasourceConfig {
    final String DATA_SOURCE = "dataSource";

    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Bean(name = DATA_SOURCE)
    public DataSource dataSource() throws Exception{
        //HikariDataSource hds = new HikariDataSource(hikariConfig());
        return new LazyConnectionDataSourceProxy(new HikariDataSource(hikariConfig()));
        // return DataSourceBuilder.create()
        //     .type(HikariDataSource.class)
        //     .build();
    }
}
