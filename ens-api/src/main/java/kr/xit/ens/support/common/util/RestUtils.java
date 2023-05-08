package kr.xit.ens.support.common.util;

import kr.xit.core.api.IRestApiResponse;
import kr.xit.core.api.RestApiErrorResponse;
import kr.xit.core.api.RestApiResponse;
import kr.xit.core.consts.Constants;
import kr.xit.ens.support.common.KakaoConstants;
import kr.xit.ens.support.kakao.model.KkoPayEltrDocDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

/**
 * <pre>
 * description :
 * packageName : kr.xit.core.support.utils
 * fileName    : RestUtils
 * author      : minuk
 * date        : 2023/05/06
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023/05/06   minuk       최초 생성
 *
 * </pre>
 */
@Slf4j
public class RestUtils {

    public static HttpHeaders setHeaders(String accessToken, String contractUuid) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType(MediaType.APPLICATION_JSON, Constants.CHARSET_UTF8));
        headers.set(KakaoConstants.HeaderName.TOKEN.getCode(), String.format("%s %s", Constants.JwtToken.GRANT_TYPE.getCode(), accessToken));
        headers.set(KakaoConstants.HeaderName.UUID.getCode(), contractUuid);
        return headers;
    }

    /**
     *
     * @param restTemplate
     * @param method
     * @param headers
     * @param body
     * @param url
     * @param clz
     * @return
     */
    public static ResponseEntity<? extends IRestApiResponse> callRestApi(RestTemplate restTemplate, HttpMethod method, HttpHeaders headers, String body, String url, Class clz) {
        String errCode = "";
        String errMsg = "";

        try {
            HttpEntity<?> entity = null;
            UriComponents uri = null;
            switch (method) {
                case GET:
                    entity = new HttpEntity<>(headers);
                    uri = UriComponentsBuilder
                            .fromHttpUrl(String.format("%s?%s", url, body == null ? "" : body))
                            //.encode(Constants.CHARSET_UTF8)
                            .build(false);
                    break;
                case POST:
                    entity = new HttpEntity<>(body, headers);
                    uri = UriComponentsBuilder
                            .fromHttpUrl(url)
                            .encode(Constants.CHARSET_UTF8)
                            .build();
                    break;
                default:
                    break;
            }
            return RestApiResponse.of(restTemplate.exchange(URI.create(uri.toString()), method, entity, clz));
            //restTemplate.exchange(URI.create(uri.toString()), method, entity, clz);

        // 4xx error
        } catch (HttpClientErrorException e) {
            errCode = String.valueOf(e.getStatusCode().value());
            errMsg = e.getResponseBodyAsString();
            log.error("call API 클라이언트오류 \n{}", e.getMessage());

        // 5xx error
        } catch (HttpServerErrorException e) {
            errCode = String.valueOf(e.getStatusCode().value());
            errMsg = e.getResponseBodyAsString();
            log.error("call API 서버오류 \n{}", e.getMessage());

        } catch (RestClientException e) {
            errCode = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errMsg = e.getMessage();
            log.error("call API \n{}", e.getMessage());

        } catch (Exception e){
            errCode = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errMsg = e.getMessage();
            log.error("call API 기타오류 \n{}", e.getMessage());
            e.printStackTrace();
        }
        return RestApiErrorResponse.of(errCode, errMsg);
    }

    public static ResponseEntity<? extends IRestApiResponse> callTestRestApi(RestTemplate restTemplate, HttpMethod method, HttpHeaders headers, String body, String url, Class clz) {
        String errCode = "";
        String errMsg = "";

        try {
            HttpEntity<?> entity = null;
            UriComponents uri = null;
            switch (method) {
                case GET:
                    entity = new HttpEntity<>(headers);
                    uri = UriComponentsBuilder
                            .fromHttpUrl(String.format("%s?%s", url, body == null ? "" : body))
                            //.encode(Constants.CHARSET_UTF8)
                            .build(false);
                    break;
                case POST:
                    entity = new HttpEntity<>(body, headers);
                    uri = UriComponentsBuilder
                            .fromHttpUrl(url)
                            .encode(Constants.CHARSET_UTF8)
                            .build();
                    break;
                default:
                    break;
            }
            StringBuilder sb = new StringBuilder()
                .append("\n=========================================================================")
                .append("\n  url => " + uri.toString())
                .append("\n  method => " + method)
                .append("\n  headers => " + entity.getHeaders().toString())
                .append("\n  body => " + entity.getBody())
                .append("\n=========================================================================");
            log.info(sb.toString());
            //return RestApiResponse.of();

            // FIXME : 결과 SET
            // String 타입
            //String rslt = "{\"doc_box_status\":\"READ\",\"doc_box_sent_at\":1616375652,\"doc_box_received_at\":1616375652,\"authenticated_at\":1616394336,\"token_used_at\":1616394336,\"doc_box_read_at\":1616397918,\"user_notified_at\":1616394336,\"payload\":\"payload 파라미터 입니다.\"}";
            //return RestApiResponse.of(rslt);

            // FIXME : 결과 SET
            // Object 타입
            return RestApiResponse.of(KkoPayEltrDocDTO.ValidTokenRes.builder()
                .token_status("USED")
                .token_expires_at(1624344762)
                .token_used_at(1624344762)
                .build());

        } catch (Exception e){
            errCode = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errMsg = e.getMessage();
            log.error("call API 기타오류 \n{}", e.getMessage());
            e.printStackTrace();
            return RestApiErrorResponse.of(errCode, errMsg);
        }

    }
}
