package kr.xit.core.spring.config.support;

import kr.xit.core.exception.BizRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

@Slf4j
public class RestTemplateResponseErrorHandler extends DefaultResponseErrorHandler {

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
        log.error("========================== RestTemplate Error  ==============================");
        log.error("SERIES NAME : {}", seriesName);
        log.error("STATUS CODE : {}", response.getStatusCode());
        log.error("=============================================================================");
    }
}
