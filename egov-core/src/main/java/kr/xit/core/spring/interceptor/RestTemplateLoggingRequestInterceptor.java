package kr.xit.core.spring.interceptor;

import kr.xit.core.spring.config.WebMvcConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
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
 * packageName : kr.xit.core.spring.interceptor
 * fileName    : RestTemplateLoggingRequestInterceptor
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 *  @see WebMvcConfig#restTemplate(RestTemplateBuilder)
 */
@Slf4j
public class RestTemplateLoggingRequestInterceptor implements ClientHttpRequestInterceptor {

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
		log.debug(
				"===========================request begin================================================");
		log.debug("URI         : {}", request.getURI());
		log.debug("Method      : {}", request.getMethod());
		log.debug("Headers     : {}", request.getHeaders());
		log.debug("Request body: {}", new String(body, Charset.defaultCharset()));
		log.debug(
				"==========================request end================================================");
	}

	private void logResponse(ClientHttpResponse response) throws IOException {
		log.debug(
				"============================response begin==========================================");
		log.debug("Status code  : {}", response.getStatusCode());
		log.debug("Status text  : {}", response.getStatusText());
		log.debug("Headers      : {}", response.getHeaders());
		log.debug("Response body: {}",
				StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
		log.debug(
				"=======================response end=================================================");
	}
}
/*
       @Bean
       @Qualifier("restTemplate")
       public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
           return restTemplateBuilder
                   // 로깅 인터셉터에서 Stream을 소비하므로 BufferingClientHttpRequestFactory 을 꼭 써야한다.
                   .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                   .setReadTimeout(Duration.ofSeconds(Constants.READ_TIMEOUT))
                   .setConnectTimeout(Duration.ofSeconds(Constants.CONNECT_TIMEOUT))
                   // UTF-8 인코딩으로 메시지 컨버터 추가
                   .messageConverters(new StringHttpMessageConverter(Constants.CHARSET))
                   // 로깅 인터셉터 설정
                   .interceptors(new RestTemplateLoggingRequestInterceptor())
                   // 로깅 인터셉터에서 Stream을 소비하므로 BufferingClientHttpRequestFactory 을 꼭 써야한다.
                   .customizers(restTemplate -> restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory())))
                   .build();
       }
 */

