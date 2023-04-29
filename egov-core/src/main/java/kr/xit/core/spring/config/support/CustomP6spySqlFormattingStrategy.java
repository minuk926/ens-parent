package kr.xit.core.spring.config.support;

import java.util.Locale;
import java.util.Objects;
import java.util.Stack;

import org.hibernate.engine.jdbc.internal.FormatStyle;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * description : SQL 로그 출력 포맷 - p6spy
 * packageName : kr.xit.core.spring.config.support
 * fileName    : CustomP6spySqlFormattingStrategy
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
@Slf4j
public class CustomP6spySqlFormattingStrategy implements MessageFormattingStrategy {
    @Override
    public String formatMessage(final int connectionId, final String now, final long elapsed, final String category, final String prepared, final String sql, final String url) {
        Stack<String> callStack = new Stack<>();
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();

        for (StackTraceElement stackTraceElement : stackTrace) {
            String trace = stackTraceElement.toString();
            if (trace.startsWith("io.p6spy") && !trace.contains("P6spyPrettySqlFormatter")) {
                callStack.push(trace);
            }
        }

        StringBuilder callStackBuilder = new StringBuilder();
        int order = 1;
        while(callStack.size() != 0) {
            callStackBuilder.append("\n\t\t").append(order++).append(". ").append(callStack.pop());
        }

        String message = new StringBuilder().append("\n\n\tConnection ID: ").append(connectionId)
                .append("\n\tExecution Time: ").append(elapsed).append(" ms\n")
                .append("\n\tCall Stack (number 1 is entry point): ").append(callStackBuilder).append("\n")
                .toString();

        return sqlFormat(sql, category, message);
    }

    private String sqlFormat(String sql, String category, String message) {
        if(sql.trim().isEmpty()) {
            return "";
        }

        if(Objects.equals(Category.STATEMENT.getName(), category)) {
            String s = sql.trim().toLowerCase(Locale.ROOT);
            if(s.startsWith("create") || s.startsWith("alter") || s.startsWith("comment") || s.startsWith("drop")) {
                sql = FormatStyle.DDL
                        .getFormatter()
                        .format(sql);
            }
            else {
                sql = FormatStyle.BASIC
                        .getFormatter()
                        .format(sql);
            }
        }

        return new StringBuilder().append("\n")
                .append("----------------------------------------------------------------------------------------------------")
                .append(sql)       //.append(sql.toUpperCase())
                .append(message)
                .append("----------------------------------------------------------------------------------------------------")
                .toString();
    }
}
