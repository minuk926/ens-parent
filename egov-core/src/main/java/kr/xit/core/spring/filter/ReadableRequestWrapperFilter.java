package kr.xit.core.spring.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * Post parameter log를 보려면 활설화 해야 함
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
