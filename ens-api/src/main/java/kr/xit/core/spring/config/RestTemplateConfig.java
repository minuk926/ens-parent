package kr.xit.core.spring.config;

import kr.xit.core.consts.Constants;
import kr.xit.core.spring.config.support.RestLoggingInterceptor;
import kr.xit.core.spring.config.support.RestResponseErrorHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.client.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Collections;

/**
 * <pre>
 * description : RestTemplate 설정
 * packageName : kr.xit.core.spring.config
 * fileName    : RestTemplateConfig
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 *  @see RestLoggingInterceptor
 *  @see RestResponseErrorHandler
 */
@Configuration
public class RestTemplateConfig {

    @Value("${spring.profiles.active:local}")
    private String activeProfile;

    @Bean
    public HttpClient httpClient() {
        RequestConfig config = RequestConfig
                .custom()
                .setConnectTimeout(Constants.CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(Constants.CONNECT_TIMEOUT)
                .setSocketTimeout(Constants.CONNECT_TIMEOUT)
                .build();

        return HttpClientBuilder
                .create()
                .setDefaultRequestConfig(config)
                .setMaxConnTotal(10)    //최대 오픈되는 커넥션 수, 연결을 유지할 최대 숫자
                .setMaxConnPerRoute(10)   //IP, 포트 1쌍에 대해 수행할 커넥션 수, 특정 경로당 최대 숫자
                .build();
    }

    /**
     * 로컬에서는 인증서 by pass
     * @return
     */
    @Bean(name = "factory")
    public ClientHttpRequestFactory factory()  {
        try{
            if(!"local".equals(activeProfile)) {
                TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

                SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();

                SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
                CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();

                HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
                requestFactory.setHttpClient(httpClient);
                requestFactory.setConnectTimeout(Constants.CONNECT_TIMEOUT);
                requestFactory.setConnectionRequestTimeout(Constants.CONNECT_TIMEOUT);
                requestFactory.setReadTimeout(Constants.READ_TIMEOUT);

                return requestFactory;
            }else{
                HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

                factory.setConnectTimeout(Constants.CONNECT_TIMEOUT);
                factory.setConnectionRequestTimeout(Constants.CONNECT_TIMEOUT);
                factory.setReadTimeout(Constants.READ_TIMEOUT);
                factory.setHttpClient(httpClient());
                return factory;
            }
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            //throw BizRuntimeException.create(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * BufferingClientHttpRequestFactory 를 사용하여 logging 처리시 setBufferRequestBody(true) : default
     * setBufferRequestBody(false) 권장
     * @param factory
     * @return
     */
    @Bean
    @DependsOn(value = {"factory"})
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = null;

        // FIXME::운영계 에서는 로깅 미사용
        if("prod".equals(activeProfile)){
            restTemplate = new RestTemplateBuilder()
                .requestFactory(() -> factory)
                // To avoid running out of memory
                .setBufferRequestBody(false)
                .setReadTimeout(Duration.ofMillis(Constants.READ_TIMEOUT))
                .setConnectTimeout(Duration.ofMillis(Constants.CONNECT_TIMEOUT))
            .build();

        }else{
            restTemplate = new RestTemplateBuilder()
                .requestFactory(() -> new BufferingClientHttpRequestFactory(factory))
                // retry / logging 인터셉터 설정
                .interceptors(new RestLoggingInterceptor())
                //.interceptors(clientHttpRetryInterceptor(), new RestTemplateLoggingRequestInterceptor())
                // To avoid running out of memory
                .setBufferRequestBody(true)
                .setReadTimeout(Duration.ofMillis(Constants.READ_TIMEOUT))
                .setConnectTimeout(Duration.ofMillis(Constants.CONNECT_TIMEOUT))
                .build();
        }
        // UTF-8 인코딩으로 메시지 컨버터 추가
        restTemplate.setMessageConverters(Collections.singletonList(new StringHttpMessageConverter(Constants.CHARSET_UTF8)));
        // 에러 핸들러
        restTemplate.setErrorHandler(new RestResponseErrorHandler());
        return restTemplate;
    }

    private ClientHttpRequestInterceptor clientHttpRetryInterceptor() {
        return (request, body, execution) -> {
            RetryTemplate retryTemplate = new RetryTemplate();
            retryTemplate.setRetryPolicy(new SimpleRetryPolicy(3));
            try {
                return retryTemplate.execute(context -> execution.execute(request, body));
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        };
    }
}
