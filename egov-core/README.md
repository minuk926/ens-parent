### log 설정 : application-${spring.profiles.active}.yml

```yml
# application-local.yml 파일 기준
app:
  # parameter 로그 출력 형식 - CUSTOM : LoggerAspect 사용,  PAYLOAD : CommonsRequestLoggingFilter 사용
  param.log.type: CUSTOM
  # param.log.type = CUSTOM인 경우 api 호출 결과 출력 여부
  result.log.trace: true
  # SQL 로그 : p6spy 
  sql.logging.enabled: true



# 로그 파일 위치
logging:
  level:
    root: debug
  file:
    path: D:/data/ens/logs
```
### SQL 로그 : p6spy 또는 log4jdbc
```yml
# SQL 로그를 log4jdbc로 교체할 경우
# pom.xml 변경 : 아래 comment 제거
      <!-- p6spy 사용으로 comment 처리
      <dependency>
      <groupId>org.bgee.log4jdbc-log4j2</groupId>
      <artifactId>log4jdbc-log4j2-jdbc4.1</artifactId>
      <version>1.16</version>
      </dependency>
      --> 
#  application-local.yml 파일 spring.datasouce의 driver 와 url 변경  
    # ================ log4jdbc ===========================
    #driver-class-name: net.sf.log4jdbc.sql.jdbcapi.DriverSpy
    #url: jdbc:log4jdbc:mariadb://211.119.124.122:3306/xplatform?useUnicode=true&characterEncoding=utf-8
    # =====================================================
    # =============== p6spy ===============================
    driver-class-name: org.mariadb.jdbc.Driver
    url: jdbc:mariadb://211.119.124.122:3306/xplatform?useUnicode=true&characterEncoding=utf-8
    # =====================================================

# sql.logging.enabled: true -> false로 변경 : p6spy log disalbled
# /resources/logback-spring-log4jdbc.xml -> logback-spring.xml 으로 변경
```

### JPA 활성 : application-jpa.yml
```yml
app:
  # jpa 활성 여부
  jpa:
    enabled: false  -> true
```
