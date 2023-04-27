package kr.xit.core.egov;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;

/**
 * @ClassName : EgovConfigAppDatasource.java
 * @Description : DataSource 설정
 *
 * @author : 윤주호
 * @since  : 2021. 7. 20
 * @version : 1.0
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일              수정자               수정내용
 *  -------------  ------------   ---------------------
 *   2021. 7. 20    윤주호               최초 생성
 * </pre>
 *
 */
@RequiredArgsConstructor
@Configuration
public class EgovConfigAppDatasource {

	private final Environment env;

	private String dbType;

	// private String className;
	//
	// private String url;
	//
	// private String userName;
	//
	// private String password;

	@PostConstruct
	void init() {
		dbType = env.getProperty("Globals.DbType");
		// className = env.getProperty("Globals." + dbType + ".DriverClassName");
		// url = env.getProperty("Globals." + dbType + ".Url");
		// userName = env.getProperty("Globals." + dbType + ".UserName");
		// password = env.getProperty("Globals." + dbType + ".Password");
	}
	
	/**
	 * @return [dataSource 설정] HSQL 설정
	 */
	private DataSource dataSourceHSQL() {
		return new EmbeddedDatabaseBuilder()
			.setType(EmbeddedDatabaseType.HSQL)
			.setScriptEncoding("UTF8")
			.addScript("classpath:/db/shtdb.sql")
			//			.addScript("classpath:/otherpath/other.sql")
			.build();
	}

	/**
	 * @return [dataSource 설정] basicDataSource 설정
	 */
@Bean
	@ConfigurationProperties("spring.datasource.hikari")
	public DataSource basicDataSource() {
		// BasicDataSource basicDataSource = new BasicDataSource();
		// basicDataSource.setDriverClassName(className);
		// basicDataSource.setUrl(url);
		// basicDataSource.setUsername(userName);
		// basicDataSource.setPassword(password);
		// return basicDataSource;

		return DataSourceBuilder.create()
			.type(HikariDataSource.class)
			.build();
	}

	/**
	 * @return [DataSource 설정]
	 */
	@Bean //n(name = {"dataSource", "egov.dataSource", "egovDataSource"})
	public DataSource dataSource() {
		if ("hsql".equals(dbType)) {
			return dataSourceHSQL();
		} else {
			return basicDataSource();
		}
	}
}
