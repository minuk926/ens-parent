### spring-boot web parameter log
```yml
# 1. actuator 의존성
implementation 'org.springframework.boot:spring-boot-starter-actuator'
# 2. yml 설정
management.endpoints.web.exposure.include: loggers
spring.mvc.log-request-details: false
logging.level.org.springframework.web: TRACE
```
         <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
