package kr.xit.core.support.logback;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * logback log masking
 * <pre>
 * <appender name="mask" class="ch.qos.logback.core.ConsoleAppender">
 *     <encoder class="ch.qos.logback.core.encoder.LayoutWrappingEncoder">
 *         <layout class="kr.xit.framework.support.logback.LogbackMaskingPatternLayout">
 * 	           <maskPattern>\"SSN\"\s*:\s*\"(.*?)\"</maskPattern> <!-- SSN JSON pattern -->
 * 	           <maskPattern>\"address\"\s*:\s*\"(.*?)\"</maskPattern> <!-- Address JSON pattern -->
 * 	           <maskPattern>(\d+\.\d+\.\d+\.\d+)</maskPattern> <!-- Ip address IPv4 pattern -->
 * 	           <maskPattern>(\w+@\w+\.\w+)</maskPattern> <!-- Email pattern -->
 * 	           <pattern>%-5p [%d{ISO8601,UTC}] [%thread] %c: %m%n%rootException</pattern>
 *         </layout>
 *     </encoder>
 * </appender>
 * </pre>
 */
/**
 * <pre>
 * description : logback log에서
 *               1) SQL 기본로그중  "==>  Preparing: " 제외
 *               2) 파라메터 출력 로그 "==> Parameters: " 제외
 *               3) Hibernate SQL 로그 제외 : org.hibernate.SQL org.hibernate.type.descriptor.sql
 * packageName : kr.xit.core.support.logback
 * fileName    : ExcloudLogFilter
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see ch.qos.logback.core.filter.Filter
 */
/*
public class LogbackMaskingPatternLayout extends PatternLayout {

    private Pattern multilinePattern;
    private List<String> maskPatterns = new ArrayList<>();

    public void addMaskPattern(String maskPattern) {
        maskPatterns.add(maskPattern);
        multilinePattern = Pattern.compile(maskPatterns.stream().collect(Collectors.joining("|")), Pattern.MULTILINE);
    }

    @Override
    public String doLayout(ILoggingEvent event) {
        return maskMessage(super.doLayout(event));
    }

    private String maskMessage(String message) {
        if (multilinePattern == null) {
            return message;
        }
        StringBuilder sb = new StringBuilder(message);
        Matcher matcher = multilinePattern.matcher(sb);
        while (matcher.find()) {
            IntStream.rangeClosed(1, matcher.groupCount()).forEach(group -> {
                if (matcher.group(group) != null) {
                    IntStream.range(matcher.start(group), matcher.end(group)).forEach(i -> sb.setCharAt(i, '*'));
                }
            });
        }
        return sb.toString();
    }
}
*/
