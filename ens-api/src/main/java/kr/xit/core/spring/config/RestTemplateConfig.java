package kr.xit.core.spring.config;

import kr.xit.core.consts.Constants;
import kr.xit.core.spring.config.support.RestTemplateLoggingInterceptor;
import kr.xit.core.spring.config.support.RestTemplateResponseErrorHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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

@Configuration
public class RestTemplateConfig {

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
                .setMaxConnTotal(100)    //최대 오픈되는 커넥션 수, 연결을 유지할 최대 숫자
                .setMaxConnPerRoute(30)   //IP, 포트 1쌍에 대해 수행할 커넥션 수, 특정 경로당 최대 숫자
                .build();
    }

    @Profile({"!local"})
    @Bean(name = "factory")
    public HttpComponentsClientHttpRequestFactory factory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

        factory.setConnectTimeout(Constants.CONNECT_TIMEOUT);
        factory.setConnectionRequestTimeout(Constants.CONNECT_TIMEOUT);
        factory.setReadTimeout(Constants.READ_TIMEOUT);
        factory.setHttpClient(httpClient());
        return factory;
    }

    @Bean
    public RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory factory) {
        return new RestTemplateBuilder()
            // 로깅 인터셉터에서 Stream을 소비하므로 BufferingClientHttpRequestFactory 을 꼭 써야한다.
            //.requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
            .requestFactory(() -> factory)
            .setReadTimeout(Duration.ofMillis(Constants.READ_TIMEOUT))
            .setConnectTimeout(Duration.ofMillis(Constants.CONNECT_TIMEOUT))
            // UTF-8 인코딩으로 메시지 컨버터 추가
            .messageConverters(new StringHttpMessageConverter(Constants.CHARSET_UTF8))
            // retry / logging 인터셉터 설정
            .interceptors(new RestTemplateLoggingInterceptor())
            //.interceptors(clientHttpRetryInterceptor(), new RestTemplateLoggingRequestInterceptor())
            // 에러 핸들러
            .errorHandler(new RestTemplateResponseErrorHandler())
            //.customizers(restTemplate -> restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory())))
            .build();
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

    /**
     * 인증서 무시
     * @return
     */
    @Profile({"local"})
    @Bean(name = "factory")
    public HttpComponentsClientHttpRequestFactory factoryLocal()  {
        try{
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();

            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();

            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);
            requestFactory.setConnectTimeout(Constants.CONNECT_TIMEOUT);
            requestFactory.setReadTimeout(Constants.READ_TIMEOUT);

            return requestFactory;
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            //throw BizRuntimeException.create(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
