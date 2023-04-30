package kr.xit.core.spring.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * description : POST request parameter logging
 * packageName : kr.xit.core.spring.filter
 * fileName    : ReadableRequestWrapperFilter
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see ReadableRequestWrapper
 * @see Filter
 */
@Slf4j
public class ReadableRequestWrapperFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
        // Do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        ReadableRequestWrapper wrapper = new ReadableRequestWrapper((HttpServletRequest)request);
        chain.doFilter(wrapper, response);
    }

    @Override
    public void destroy() {
        // Do nothing
    }
}
