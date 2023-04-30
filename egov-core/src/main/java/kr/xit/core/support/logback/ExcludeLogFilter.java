package kr.xit.core.support.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

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
 * @see Filter
 */
public class ExcludeLogFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (event.getMessage().contains("==>  Preparing:")
            || event.getMessage().contains("==> Parameters:")
            || event.getMessage().contains("org.hibernate.SQL")
            || event.getMessage().contains("org.hibernate.type.descriptor.sql")
        ) {
            return FilterReply.DENY; //DENY |  NEUTRAL |   ACCEPT
        } else {
            return FilterReply.ACCEPT;
        }
    }
}

