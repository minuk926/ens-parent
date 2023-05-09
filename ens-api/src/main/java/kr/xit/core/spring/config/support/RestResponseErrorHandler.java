package kr.xit.core.spring.config.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

/**
 * <pre>
 * description : RestTemplate Error Handler
 * packageName : kr.xit.core.spring.config.support
 * fileName    : RestResponseErrorHandler
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
public class RestResponseErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String seriesName = "UNKNOWN_ERROR";

        if (response.getStatusCode() == HttpStatus.PRECONDITION_FAILED) {
            seriesName = String.valueOf(HttpStatus.PRECONDITION_FAILED.value());

        }else if (response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
            // handle SERVER_ERROR
            seriesName = HttpStatus.Series.SERVER_ERROR.name();

        } else if (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
            // handle CLIENT_ERROR
            seriesName = HttpStatus.Series.CLIENT_ERROR.name();
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                seriesName = String.valueOf(HttpStatus.NOT_FOUND.value());
            }
        }
        log.error("========================== RestResponse Error  ==============================");
        log.error("SERIES NAME : {}", seriesName);
        log.error("STATUS CODE : {}", response.getStatusCode());
        log.error("=============================================================================");
    }
}
