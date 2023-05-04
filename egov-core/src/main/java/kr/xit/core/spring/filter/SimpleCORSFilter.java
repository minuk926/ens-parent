package kr.xit.core.spring.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import kr.xit.core.spring.config.properties.CorsProperties;
import kr.xit.core.spring.util.SpringUtils;

/**
 * <pre>
 * description : Cors filter
 * packageName : kr.xit.core.spring.filter
 * fileName    : SimpleCORSFilter
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
public class SimpleCORSFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		CorsProperties corsProperties = SpringUtils.getCorsProperties();
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setHeader("Access-Control-Allow-Origin", corsProperties.getAllowedOrigins());
		httpResponse.setHeader("Access-Control-Allow-Methods", corsProperties.getAllowedMethods());
		httpResponse.setHeader("Access-Control-Allow-Headers", corsProperties.getAllowedHeaders());
		httpResponse.setHeader("Access-Control-Allow-Credentials", corsProperties.getAllowCredentials().toString());
		httpResponse.setHeader("Access-Control-Max-Age", corsProperties.getMaxAge().toString());
		httpResponse.setHeader("Access-Control-Expose-Headers", corsProperties.getExposeHeader());
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) {
		// Do nothing
	}

	@Override
	public void destroy() {
		// Do nothing
	}
}
