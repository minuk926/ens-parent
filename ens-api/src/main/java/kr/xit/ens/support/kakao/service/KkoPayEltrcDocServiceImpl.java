package kr.xit.ens.support.kakao.service;

import kr.xit.core.api.IRestApiResponse;
import kr.xit.core.api.RestApiErrorResponse;
import kr.xit.core.api.RestApiResponse;
import kr.xit.core.consts.Constants;
import kr.xit.core.support.utils.JsonUtil;
import kr.xit.ens.support.common.KakaoConstants;
import kr.xit.ens.support.kakao.dto.KkoPayEltrDocDTO;
import lombok.extern.slf4j.Slf4j;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * <pre>
 * description : 카카오 페이 전자 문서 발송 요청 서비스
 * packageName : kr.xit.ens.support.kakao.service
 * fileName    : KkoPayEltrcDocServiceImpl
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
@Slf4j
@Component
@Profile({"!local-test"})
public class KkoPayEltrcDocServiceImpl extends EgovAbstractServiceImpl implements IKkoPayEltrcDocService {

    @Value("${contract.kakao.pay.mydoc.access-token}")
    private String accessToken;
    @Value("${contract.kakao.pay.mydoc.contract-uuid}")
    private String contractUuid;
    @Value("${contract.kakao.pay.mydoc.host}")
    private String HOST;
    @Value("${contract.kakao.pay.mydoc.api.send}")
    private String API_SEND;
    @Value("${contract.kakao.pay.mydoc.api.token}")
    private String API_TOKEN;
    @Value("${contract.kakao.pay.mydoc.api.readcompleted}")
    private String API_READCOMPLETED;
    @Value("${contract.kakao.pay.mydoc.api.status}")
    private String API_STATUS;
    @Value("${contract.kakao.pay.mydoc.api.bulksend}")
    private String API_BULKSEND;
    @Value("${contract.kakao.pay.mydoc.api.bulkstatus}")
    private String API_BULKSTATUS;

    /**
     * 모바일웹 연계 문서발송 요청
     * -.이용기관 서버에서 전자문서 서버로 문서발송 처리를 요청합니다.
     */
    /**
     *
     * @param accessToken
     * @param contractUuid
     * @param jsonStr
     * @return
     */
    //@Override
    public ResponseEntity<? extends IRestApiResponse> requestSend(final KkoPayEltrDocDTO.RequestSendReq reqDTO) {
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        // 실제 데이타만 검증
        Set<ConstraintViolation<KkoPayEltrDocDTO.RequestSend>> list = validator.validate(reqDTO.getDocument());
        if (list.size() > 0) {

            List<String> errors = list.stream()
                .map(row -> String.format("%s=%s", row.getPropertyPath(), row.getMessageTemplate()))
                .collect(Collectors.toList());

            return RestApiErrorResponse.of(errors.toString());
        }

        HttpHeaders headers = new HttpHeaders();
        //		headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentType(new MediaType(MediaType.APPLICATION_JSON, Charset.forName("utf-8")));
        headers.set(KakaoConstants.HeaderName.TOKEN.getCode(), String.format("%s %s", Constants.JwtToken.GRANT_TYPE.getCode(), accessToken));
        headers.set(KakaoConstants.HeaderName.UUID.getCode(), contractUuid);

        StringBuilder url = new StringBuilder()
            .append(HOST)
            .append(API_SEND);

        ResponseEntity<String> resp = this.callApi(HttpMethod.GET, url.toString(), JsonUtil.toJson(reqDTO), headers);

        return RestApiResponse.of(reqDTO);
    }


//     /**
//      * 문서 상태 변경 API
//      */
//     @Override
//     public ResponseEntity<String> readCompleted(String accessToken, String documentBinderUuid) {
//
//         HttpHeaders headers = new HttpHeaders();
// //		headers.setContentType(MediaType.APPLICATION_JSON);
//         headers.setContentType(new MediaType(MediaType.APPLICATION_JSON, Charset.forName("utf-8")));
//         headers.set("Authorization", String.format("Bearer %s", accessToken));
//         headers.set("X-Xit-DBUuid", documentBinderUuid);
//
//
//         StringBuilder url = new StringBuilder();
//         url.append(this.HOST)
//                 .append(API_READCOMPLETED.replace("{documentBinderUuid}", documentBinderUuid == null ? "" : documentBinderUuid));
//
//
//
//         String jsonStr = "{                                 "
//                 + "    \"document\" :{               "
//                 + "        \"is_detail_read\" : true "
//                 + "    }                             "
//                 + "}                                 ";
//
//
//
//         ResponseEntity<String> resp = this.callApi(HttpMethod.POST, url.toString(), jsonStr, headers);
//         return resp;
//
//     }
//
//
//     /**
//      * 문서 상태 조회 API
//      */
//     @Override
//     public ResponseEntity<String> status(String accessToken, String contractUuid, String documentBinderUuid) {
//
//         HttpHeaders headers = new HttpHeaders();
// //		headers.setContentType(MediaType.APPLICATION_JSON);
//         headers.setContentType(new MediaType(MediaType.APPLICATION_JSON, Charset.forName("utf-8")));
//         headers.set("Authorization", String.format("Bearer %s", accessToken));
//         headers.set("Contract-Uuid", contractUuid);
//         headers.set("X-Xit-DBUuid", documentBinderUuid);
//
//
//         StringBuilder url = new StringBuilder();
//         url.append(this.HOST)
//                 .append(API_STATUS.replace("{documentBinderUuid}", documentBinderUuid == null ? "" : documentBinderUuid));
//
//
//         ResponseEntity<String> resp = this.callApi(HttpMethod.GET, url.toString(), null, headers);
//         return resp;
//
//     }
//
//
//     /**
//      * 대량(bulk) 문서발송 요청
//      * -.이용기관 서버에서 카카오페이 내문서함 서버로 대량(bulk) 문서발송 처리를 요청합니다.
//      */
// //	POST /v1/documents/bulk HTTP/1.1
// //	Host: docs-gw.kakaopay.com (전용선/VPN 일 경우 docs-gw-gs.kakaopay.com)
// //	Content-Type: application/json;charset=UTF-8
// //	Authorization: Bearer {Access_Token}
// //	Contract-Uuid: {Contract-Uuid}
//     @Override
//     public ResponseEntity<String> bulkSend(String accessToken, String contractUuid, String jsonStr) {
//
//         HttpHeaders headers = new HttpHeaders();
// //		headers.setContentType(MediaType.APPLICATION_JSON);
//         headers.setContentType(new MediaType(MediaType.APPLICATION_JSON, Charset.forName("utf-8")));
//         headers.set("Authorization", String.format("Bearer %s", accessToken));
//         headers.set("Contract-Uuid", contractUuid);
//
//
//         StringBuilder url = new StringBuilder();
//         url.append(this.HOST)
//                 .append(API_BULKSEND);
//
//
//         ResponseEntity<String> resp = this.callApi(HttpMethod.POST, url.toString(), jsonStr, headers);
//         return resp;
//
//     }
//
//
//     /**
//      * 대량(bulk) 문서 상태 조회 API
//      * -.이용기관 서버에서 카카오페이 전자문서 서버로 문서 상태에 대한 조회를 요청 합니다.
//      * : 발송된 문서의 진행상태를 알고 싶은 경우, flow와 상관없이 요청 가능
//      * : polling 방식으로 호출할 경우, 호출 간격은 5초를 권장.
//      * -.doc_box_status 상태변경순서
//      * : SENT(송신) > RECEIVED(수신) > READ(열람)/EXPIRED(미열람자료의 기한만료)
//      */
// //	POST /v1/documents/bulk/status HTTP/1.1
// //	Host: docs-gw.kakaopay.com (전용선/VPN 일 경우 docs-gw-gs.kakaopay.com)
// //	Content-Type: application/json;charset=UTF-8
// //	Authorization: Bearer {Access_Token}
// //	Contract-Uuid: {Contract-Uuid}
//     @Override
//     public ResponseEntity<String> bulkStatus(String accessToken, String contractUuid, String jsonStr) {
//
//         HttpHeaders headers = new HttpHeaders();
// //		headers.setContentType(MediaType.APPLICATION_JSON);
//         headers.setContentType(new MediaType(MediaType.APPLICATION_JSON, Charset.forName("utf-8")));
//         headers.set("Authorization", String.format("Bearer %s", accessToken));
//         headers.set("Contract-Uuid", contractUuid);
//
//
//         StringBuilder url = new StringBuilder();
//         url.append(this.HOST)
//                 .append(API_BULKSTATUS);
//
//
//         ResponseEntity<String> resp = this.callApi(HttpMethod.POST, url.toString(), jsonStr, headers);
//         return resp;
//     }


    /**
     * <pre>메소드 설명: API 호출
     * </pre>
     *
     * @param method
     * @param url
     * @param body
     * @param headers
     * @return ResponseEntity 요청처리 후 응답객체
     * @author: 박민규
     * @date: 2021. 8. 4.
     * @apiNote: 사이트 참조 https://e2e2e2.tistory.com/15
     */
    private ResponseEntity<String> callApi(HttpMethod method, String url, String body, HttpHeaders headers) {

        StringBuffer sb = new StringBuffer();
        ResponseEntity<String> responseEntity = null;
        try {

            HttpEntity<?> entity = null;
            UriComponents uri = null;
            switch (method) {
                case GET:
                    entity = new HttpEntity<>(headers);
                    uri = UriComponentsBuilder
                            .fromHttpUrl(String.format("%s?%s", url, body == null ? "" : body))
//							.encode(StandardCharsets.UTF_8)	//"%"기호가 "%25"로 인코딩 발생하여 주석처리 함.
                            .build(false);
                    break;
                case POST:
                    entity = new HttpEntity<>(body, headers);
                    uri = UriComponentsBuilder
                            .fromHttpUrl(url)
                            .encode(StandardCharsets.UTF_8)
                            .build();
                    break;

                default:
                    break;
            }

            HttpComponentsClientHttpRequestFactory factory = null;
try {
    factory = new HttpComponentsClientHttpRequestFactory();
}catch (Exception e){
    e.printStackTrace();
}
            factory.setConnectTimeout(3000);
//			factory.setReadTimeout(3000);
            factory.setReadTimeout(10000);


            RestTemplate restTemplate = new RestTemplate(factory);
            sb.append("\n  url => " + uri.toString())
                    .append("\n  method => " + method)
                    .append("\n  headers => " + entity.getHeaders().toString())
                    .append("\n  body => " + entity.getBody());
            responseEntity = restTemplate.exchange(URI.create(uri.toString()), method, entity, String.class);

            /*
             * HttpStatus 정보 확인 방법
             * 	-.코드: responseEntity.getStatusCodeValue()
             * 	-.메시지: responseEntity.getStatusCode()
             */

        } catch (HttpServerErrorException e) {
            responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
            log.error("call API 서버오류\n[ url ]: {} \n[ param ]: {} \n[ error ]: {}", url, body, e.getMessage());
        } catch (HttpClientErrorException e) {
            responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
            log.error("call API 클라이언트오류\n[ url ]: {} \n[ param ]: {} \n[ error ]: {}", url, body, e.getMessage());
        } catch (RestClientException e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.REQUEST_TIMEOUT);
            log.error("RestAPI 호출 오류\n[ url ]: {} \n[ param ]: {} \n[ error ]: {}", url, body, e.getMessage());
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            log.error("call API 기타오류\n[ url ]: {} \n[ param ]: {} \n[ error ]: {}", url, body, e.getMessage());
        } finally {
            log.info("카카오페이 내문서함(인증톡) API\n[ REQUEST ]-----------------------------------------------------------------------\n{}\n[ RESPONSE ]-----------------------------------------------------------------------\n{}", sb.toString(), responseEntity.getBody());
        }


        return responseEntity;
    }

}

