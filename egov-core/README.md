
## 전자정부 프레임워크 호환성
[호환성 가이드 문서](./egov_compatibility_guide.md)

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

### spring validation
```text
@Valid는 Java, @Validated는 Spring에서 지원하는 어노테이션
@Validated는 @Valid의 기능을 포함하고, 유효성을 검토할 그룹을 지정할 수 있는 기능이 추가됨
```

```java
@Null       // null만 혀용
@NotNull    // null을 허용하지 않습니다. "", " "는 허용
@NotEmpty   // null, ""을 허용하지 않습니다. " "는 허용
@NotBlank   // null, "", " " 모두 허용하지 않습니다.

@Email              // 이메일 형식을 검사합니다. 다만 ""의 경우를 통과 시킵니다
@Pattern(regexp = ) // 정규식을 검사할 때 사용됩니다.
@Size(min=, max=)   // 길이를 제한할 때 사용됩니다.

@Max(value = )      // value 이하의 값을 받을 때 사용됩니다.
@Min(value = )      // value 이상의 값을 받을 때 사용됩니다.

@Positive           // 값을 양수로 제한합니다.
@PositiveOrZero     // 값을 양수와 0만 가능하도록 제한합니다.

@Negative           // 값을 음수로 제한합니다.
@NegativeOrZero     // 값을 음수와 0만 가능하도록 제한합니다.

@Future         // 현재보다 미래
@Past           // 현재보다 과거

@AssertFalse    // false 여부, null은 체크하지 않습니다.
@AssertTrue     // true 여부, null은 체크하지 않습니다.
```
