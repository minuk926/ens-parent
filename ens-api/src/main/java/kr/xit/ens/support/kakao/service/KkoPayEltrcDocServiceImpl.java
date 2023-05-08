package kr.xit.ens.support.kakao.service;

import kr.xit.core.api.IRestApiResponse;
import kr.xit.core.api.RestApiErrorResponse;
import kr.xit.core.exception.BizRuntimeException;
import kr.xit.core.support.utils.Checks;
import kr.xit.core.support.utils.JsonUtils;
import kr.xit.ens.support.common.util.RestUtils;
import kr.xit.ens.support.kakao.model.KkoPayEltrDocDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
@RequiredArgsConstructor
@Component
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
    private String API_MODIFY_STATUS;
    @Value("${contract.kakao.pay.mydoc.api.status}")
    private String API_STATUS;
    @Value("${contract.kakao.pay.mydoc.api.bulksend}")
    private String API_BULKSEND;
    @Value("${contract.kakao.pay.mydoc.api.bulkstatus}")
    private String API_BULKSTATUS;

    private final RestTemplate restTemplate;



    /**
     * 모바일웹 연계 문서발송 요청
     * -.이용기관 서버에서 전자문서 서버로 문서발송 처리를 요청합니다.
     */
    /**
     *
     * @param reqDTO KkoPayEltrDocDTO.RequestSendReq
     * @return
     */
    @Override
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

        KkoPayEltrDocDTO.Receiver receiver = reqDTO.getDocument().getReceiver();
        if(Checks.isEmpty(receiver.getCi())){
            if(Checks.isEmpty(receiver.getName())) throw BizRuntimeException.create("받는이 이름은 필수입니다.");
            if(Checks.isEmpty(receiver.getPhone_number())) throw BizRuntimeException.create("받는이 전화번호는 필수입니다.");
            if(Checks.isEmpty(receiver.getBirthday())) throw BizRuntimeException.create("받는이 생년월일은 필수입니다.");
        }else{

        }

        HttpHeaders headers = RestUtils.setHeaders(accessToken, contractUuid);
        StringBuilder url = new StringBuilder()
            .append(HOST)
            .append(API_SEND);

        return RestUtils.callRestApi(restTemplate, HttpMethod.POST, headers, JsonUtils.toJson(reqDTO), url.toString(), String.class);
    }

    @Override
    public ResponseEntity<? extends IRestApiResponse> validToken(final KkoPayEltrDocDTO.ValidTokenReq reqDTO) {
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        // 실제 데이타만 검증
        Set<ConstraintViolation<KkoPayEltrDocDTO.ValidTokenReq>> list = validator.validate(reqDTO);
        if (list.size() > 0) {

            List<String> errors = list.stream()
                .map(row -> String.format("%s=%s", row.getPropertyPath(), row.getMessageTemplate()))
                .collect(Collectors.toList());

            return RestApiErrorResponse.of(errors.toString());
        }

        HttpHeaders headers = RestUtils.setHeaders(accessToken, contractUuid);
        StringBuilder url = new StringBuilder()
            .append(HOST)
            .append(
                API_TOKEN.replace("{document_binder_uuid}", reqDTO.getDocument_binder_uuid())
                         .replace("{token}", reqDTO.getToken())
            );
        return RestUtils.callRestApi(restTemplate, HttpMethod.GET, headers, null, url.toString(), KkoPayEltrDocDTO.ValidTokenRes.class);
    }

    @Override
    public ResponseEntity<? extends IRestApiResponse> modifyStatus(final String document_binder_uuid){
        if(Checks.isEmpty(document_binder_uuid) || document_binder_uuid.length() > 40){
            throw BizRuntimeException.create("문서 식별 번호는 필수입니다(max:40자)");
        }

        String body = "{\"document\": {\"is_detail_read\": true} }";
        HttpHeaders headers = RestUtils.setHeaders(accessToken, contractUuid);
        StringBuilder url = new StringBuilder()
            .append(HOST)
            .append(API_MODIFY_STATUS.replace("{document_binder_uuid}", document_binder_uuid));
        return RestUtils.callRestApi(restTemplate, HttpMethod.POST, headers, body, url.toString(), String.class);
    }

    @Override
    public ResponseEntity<? extends IRestApiResponse> findStatus(final String document_binder_uuid){
        if(Checks.isEmpty(document_binder_uuid) || document_binder_uuid.length() > 40){
            throw BizRuntimeException.create("문서 식별 번호는 필수입니다(max:40자)");
        }

        HttpHeaders headers = RestUtils.setHeaders(accessToken, contractUuid);
        StringBuilder url = new StringBuilder()
            .append(HOST)
            .append(API_STATUS.replace("{document_binder_uuid}", document_binder_uuid));
        return RestUtils.callRestApi(restTemplate, HttpMethod.GET, headers, null, url.toString(), KkoPayEltrDocDTO.DocStatusRes.class);
    }

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



}

