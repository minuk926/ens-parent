package kr.xit.core.spring.config.support;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * <pre>
 * description : RestTemplate 로깅 Interceptor
 *               - 반드시 RestTemplate의 Factory 변경 필요
 * packageName : kr.xit.core.spring.config.support
 * fileName    : RestLoggingInterceptor
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 *  @see kr.xit.core.spring.config.RestTemplateConfig#restTemplate
 */
@Slf4j
public class RestLoggingInterceptor implements ClientHttpRequestInterceptor {

	/**
	 * RestTemplate 로깅 Interceptor
	 *
	 * @param request HttpRequest
	 * @param body Request Body
	 * @param execution ClientHttpRequestExecution
	 * @return ClientHttpResponse
	 */
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		logRequest(request, body);
		ClientHttpResponse response = execution.execute(request, body);
		logResponse(response);
		return response;
	}

	private void logRequest(HttpRequest request, byte[] body) throws IOException {
		log.info("=========================== Rest Request Parameter =================================");
		log.info("URI         : {}", request.getURI());
		log.info("Method      : {}", request.getMethod());
		log.info("Headers     : {}", request.getHeaders());
		log.info("Request body: {}", new String(body, Charset.defaultCharset()));
		log.info("====================================================================================");
	}

	private void logResponse(ClientHttpResponse response) throws IOException {
		log.info("============================ Rest Response Result ===============================");
		log.info("Status code  : {}", response.getStatusCode());
		log.info("Status text  : {}", response.getStatusText());
		log.info("Headers      : {}", response.getHeaders());
		log.info("Response body: {}", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
		log.info("=================================================================================");
	}
}
