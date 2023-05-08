package kr.xit.ens.support.kakao.web;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.xit.core.api.RestApiErrorResponse;
import kr.xit.core.api.RestApiResponse;
import kr.xit.core.exception.BizRuntimeException;
import kr.xit.core.support.utils.Checks;
import kr.xit.ens.support.kakao.validator.EstlRequestValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import kr.xit.core.api.IRestApiResponse;
import kr.xit.ens.support.kakao.model.KkoPayEltrDocDTO;
import kr.xit.ens.support.kakao.service.IKkoPayEltrcDocService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "KkoPayEltrcDocController", description = "카카오페이 전자문서 발송")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/kakaopay/v1")
public class KkoPayEltrcDocController {
    @Value("${contract.kakao.pay.mydoc.access-token}")
    private String accessToken;

    private final IKkoPayEltrcDocService kkoPayEltrcDocService;
    private final EstlRequestValidator estlRequestValidator;

    @Operation(summary = "문서발송 요청", description = "카카오페이 전자문서 서버로 문서발송 처리를 요청")
    @PostMapping(value = "/documents", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends IRestApiResponse> requestSend(
        @Validated @RequestBody final KkoPayEltrDocDTO.RequestSendReq reqDTO
    ) {
        KkoPayEltrDocDTO.Receiver receiver = reqDTO.getDocument().getReceiver();
        if(Checks.isEmpty(receiver.getCi())){
            if(Checks.isEmpty(receiver.getName())) throw BizRuntimeException.create("받는이 이름은 필수입니다.");
            if(Checks.isEmpty(receiver.getPhone_number())) throw BizRuntimeException.create("받는이 전화번호는 필수입니다.");
            if(Checks.isEmpty(receiver.getBirthday())) throw BizRuntimeException.create("받는이 생년월일은 필수입니다.");
        }else{

        }
        return kkoPayEltrcDocService.requestSend(reqDTO);
    }

    @Operation(summary = "One Time Token 전송", description = "Redirect URL 접속 시도")
    @Parameters({
        @Parameter(in = ParameterIn.QUERY, name = "document_binder_uuid", required = true, description = "카카오페이 문서 식별 번호(max:40)", example = "BIN-ff806328863311ebb61432ac599d6150"),
        @Parameter(in = ParameterIn.QUERY, name = "token", required = true, description = "카카오페이 전자문서 서버 토큰(max:50)", example = "CON-cc375944ae3d11ecb91e42193199ee3c"),
        @Parameter(in = ParameterIn.QUERY, name = "external_document_uuid", description = "외부 문서 식별 번호(max:40)", example = "A00001")
    })
    @GetMapping(value = "/documents", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends IRestApiResponse> oneTimeTokenSend(
            @Parameter(hidden = true)
            @ModelAttribute final KkoPayEltrDocDTO.EstlRequest reqDTO,
            BindingResult bindingResult
    ) {

        estlRequestValidator.validate(reqDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return RestApiErrorResponse.of(bindingResult);
        }

        return RestApiResponse.of();
    }

    @Operation(summary = "One Time Token 전송", description = "Redirect URL 접속 시도")
    @PostMapping(value = "/documents2", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends IRestApiResponse> oneTimeTokenSend2(
            @Validated @RequestBody final KkoPayEltrDocDTO.EstlRequest reqDTO
    ) {
        return RestApiResponse.of();
    }

    @Operation(summary = "토큰 유효성 검증", description = "Redirect URL 접속 허용/불허")
    @PostMapping(value = "/validToken", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends IRestApiResponse> validToken(
        @Validated @RequestBody final KkoPayEltrDocDTO.ValidTokenReq reqDTO
    ) {
        return kkoPayEltrcDocService.validToken(reqDTO);
    }

    /**
     * 문서에 대한 열람 상태 변경
     * 사용자가 문서 열람시(토큰유효성 검증 완료후 페이지 로딩 완료 시점) 반드시 호출
     * 미호출시
     *  - 유통증명시스템 사용시 : 해당 API 호출 시점으로 열람 정보가 등록되어 열람정보 등록 불가
     *  - 문서상태 조회 API 호출시 최초열람시간(read_at) 수신 불가
     *
     * 정상 응답 : body없이 204 status return
     * @param document_binder_uuid
     * @return
     */
    @Operation(summary = "문서 상태 변경", description = "문서 상태 변경")
    @PostMapping(value = "/modifyStatus", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends IRestApiResponse> modifyStatus(
        @Validated @RequestBody final KkoPayEltrDocDTO.DocStatusReq reqDTO
    ) {
        return kkoPayEltrcDocService.modifyStatus(reqDTO.getDocument_binder_uuid());
    }

    /**
     * 문서 상태 조회 API
     * -.이용기관 서버에서 카카오페이 전자문서 서버로 문서 상태에 대한 조회를 요청 합니다.
     * : 발송된 문서의 진행상태를 알고 싶은 경우, flow와 상관없이 요청 가능
     * : polling 방식으로 호출할 경우, 호출 간격은 5초를 권장.
     * -.doc_box_status 상태변경순서
     * : SENT(송신) > RECEIVED(수신) > READ(열람)/EXPIRED(미열람자료의 기한만료)
     */
    @Operation(summary = "문서 상태 조회", description = "문서 상태 조회")
    @PostMapping(value = "/findStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends IRestApiResponse> findStatus(
        @Validated @RequestBody final KkoPayEltrDocDTO.DocStatusReq reqDTO
    ) {
        return kkoPayEltrcDocService.findStatus(reqDTO.getDocument_binder_uuid());
    }











//     @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = {
//             @Content(mediaType = "application/json", examples = {
//                     @ExampleObject(name = "Sample Example..."
//                             , summary = "제작"
//                             , value = "{\"sendMastId\": 1}")
//             })
//     })
//     @Operation(summary = "제작")
// //    @PutMapping(value = "/kko/alimtalk/make", produces = MediaType.APPLICATION_JSON_VALUE)
//     @PostMapping(value = "/kko/alimtalk/make", produces = MediaType.APPLICATION_JSON_VALUE)
//     public ResponseEntity<EnsResponseVO> make(@RequestBody Map<String, Long> mParam) {
//
//         EnsResponseVO responseVO = kkoAlimtalkService.make(mParam.get("sendMastId"));
//
//         return new ResponseEntity<EnsResponseVO>(responseVO, HttpStatus.OK);
//     }
//
//     @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = {
//             @Content(mediaType = "application/json", examples = {
//                     @ExampleObject(name = "Sample Example..."
//                             , summary = "제작(청구서링크)"
//                             , value = "{\"sendMastId\": 1}")
//             })
//     })
//     @Operation(summary = "제작(템플릿메시지)")
// //    @PutMapping(value = "/kko/alimtalk/make/tmpltmsg", produces = MediaType.APPLICATION_JSON_VALUE)
//     @PostMapping(value = "/kko/alimtalk/make/tmpltmsg", produces = MediaType.APPLICATION_JSON_VALUE)
//     public ResponseEntity<EnsResponseVO> makeTmpltMsg(@RequestBody Map<String, Long> mParam) {
//
//         EnsResponseVO responseVO = kkoAlimtalkService.makeTmpltMsg(mParam.get("sendMastId"));
//
//         return new ResponseEntity<EnsResponseVO>(responseVO, HttpStatus.OK);
//     }
//
//
//     @Operation(summary = "제작(일괄)")
// //    @PutMapping(value = "/kko/alimtalk/make/all", produces = MediaType.APPLICATION_JSON_VALUE)
//     @PostMapping(value = "/kko/alimtalk/make/all", produces = MediaType.APPLICATION_JSON_VALUE)
//     public ResponseEntity<EnsResponseVO> makeAll() {
//
//         EnsResponseVO responseVO = kkoAlimtalkService.makeAll();
//
//         return new ResponseEntity<EnsResponseVO>(responseVO, HttpStatus.OK);
//     }
//
//
//     @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = {
//             @Content(mediaType = "application/json", examples = {
//                     @ExampleObject(name = "Sample Example..."
//                             , summary = "(대량)전송요청"
//                             , value = "{\"sendMastId\": 1}")
//             })
//     })
//     @Operation(summary = "(대량)전송요청")
//     @PostMapping(value = "/kko/alimtalk/send/bulk", produces = MediaType.APPLICATION_JSON_VALUE)
//     public ResponseEntity<EnsResponseVO> sendBulk(@RequestBody Map<String, Long> mParam) {
//         EnsResponseVO responseVO = kkoAlimtalkService.sendBulk(mParam.get("sendMastId"));
//
//         return new ResponseEntity<EnsResponseVO>(responseVO, HttpStatus.OK);
//     }
//
//     @Operation(summary = "(대량)전송요청 일괄")
//     @PostMapping(value = "/kko/alimtalk/send/bulk/all", produces = MediaType.APPLICATION_JSON_VALUE)
//     public ResponseEntity<EnsResponseVO> sendBulkAll() {
//         EnsResponseVO responseVO = kkoAlimtalkService.sendBulkAll();
//
//         return new ResponseEntity<EnsResponseVO>(responseVO, HttpStatus.OK);
//     }
//
//
//     @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = {
//             @Content(mediaType = "application/json", examples = {
//                     @ExampleObject(name = "Sample Example..."
//                             , summary = "(대량)문서상태 갱신"
//                             , value = "{\"sendMastId\": 1, \"btToken\": \"eyJhbGciOiJIUzI1NiJ9.eyJic2lkIjoia290aSIsImV4cCI6MTY0NDQwMjI3MywiaWF0IjoxNjQ0MzE1ODczLCJpcEFkZHIiOiIyMTEuMTE5LjEyNC40MiJ9.7gAQF0FJrVAsRg2umAULa4R7UZFCjnk49Vyo02mYYlU\"}")
//             })
//     })
//     @Operation(summary = "(대량)문서상태 갱신")
// //    @PutMapping(value = "/kko/alimtalk/stat/bulk", produces = MediaType.APPLICATION_JSON_VALUE)
//     @PostMapping(value = "/kko/alimtalk/stat/bulk", produces = MediaType.APPLICATION_JSON_VALUE)
//     public ResponseEntity<EnsResponseVO> statBulk(@RequestBody Map<String, Object> mParam) {
//
//         EnsResponseVO responseVO = kkoAlimtalkService.statBulk((String) mParam.get("btToken"), Long.valueOf((Integer) mParam.get("sendMastId")));
//
//         return new ResponseEntity<EnsResponseVO>(responseVO, HttpStatus.OK);
//     }
//
//
//     @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = {
//             @Content(mediaType = "application/json", examples = {
//                     @ExampleObject(name = "Sample Example..."
//                             , summary = "전송결과 가져오기"
//                             , value = "{\"sendMastId\": 1}")
//             })
//     })
//     @Operation(summary = "전송결과 가져오기")
//     @PostMapping(value = "/kko/alimtalk/send/result/provide", produces = MediaType.APPLICATION_JSON_VALUE)
//     public ResponseEntity<EnsResponseVO> sendResultProvide(@RequestBody Map<String, Long> mParam) {
//
//         EnsResponseVO responseVO = kkoAlimtalkService.sendResultProvide(mParam.get("sendMastId"));
//
//
//         return new ResponseEntity<EnsResponseVO>(responseVO, HttpStatus.OK);
//     }
}
