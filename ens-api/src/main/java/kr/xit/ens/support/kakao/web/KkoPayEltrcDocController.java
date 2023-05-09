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

    /**
     * <pre>
     * 모바일웹 연계 문서발송 요청
     * -.이용기관 서버에서 전자문서 서버로 문서발송 처리를 요청합니다.
     * </pre>
     * @param reqDTO
     * @return ResponseEntity
     */
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

    /**
     * <pre>
     * 토큰 유효성 검증(Redirect URL  접속 허용/불허)
     * </pre>
     * @param reqDTO KkoPayEltrDocDTO.RequestSendReq
     * @return ResponseEntity
     */
    @Operation(summary = "토큰 유효성 검증", description = "Redirect URL 접속 허용/불허")
    @PostMapping(value = "/validToken", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends IRestApiResponse> validToken(
        @Validated @RequestBody final KkoPayEltrDocDTO.ValidTokenReq reqDTO
    ) {
        return kkoPayEltrcDocService.validToken(reqDTO);
    }

    /**
     * <pre>
     * 문서 상태 변경 API
     * -.문서에 대해서 열람 상태로 변경. 사용자가 문서열람 시(OTT 검증 완료 후 페이지 로딩 완료 시점) 반드시 문서 열람 상태 변경 API를 호출해야 함.
     * -.미 호출 시 아래와 같은 문제 발생
     * 1)유통증명시스템을 사용하는 경우 해당 API를 호출한 시점으로 열람정보가 등록되어 미 호출 시 열람정보가 등록 되지 않음.
     * 2)문서상태조회 API(/v1/documents/{document_binder_uuid}/status) 호출 시 read_at최초 열람시간) 데이터가 내려가지 않음.
     * </pre>
     * @param reqDTO KkoPayEltrDocDTO.DocumentBinderUuid
     * @return ResponseEntity
     */
    @Operation(summary = "문서 상태 변경", description = "문서 상태 변경")
    @PostMapping(value = "/modifyStatus", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends IRestApiResponse> modifyStatus(
        @Validated @RequestBody final KkoPayEltrDocDTO.DocumentBinderUuid reqDTO
    ) {
        return kkoPayEltrcDocService.modifyStatus(reqDTO);
    }

    /**
     * <pre>
     * 문서 상태 조회 API
     * -.이용기관 서버에서 카카오페이 전자문서 서버로 문서 상태에 대한 조회를 요청 합니다.
     * : 발송된 문서의 진행상태를 알고 싶은 경우, flow와 상관없이 요청 가능
     * : polling 방식으로 호출할 경우, 호출 간격은 5초를 권장.
     * -.doc_box_status 상태변경순서
     * : SENT(송신) > RECEIVED(수신) > READ(열람)/EXPIRED(미열람자료의 기한만료)
     * </pre>
     * @param reqDTO KkoPayEltrDocDTO.DocumentBinderUuid
     * @return ResponseEntity
     */
    @Operation(summary = "문서 상태 조회", description = "문서 상태 조회")
    @PostMapping(value = "/findStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends IRestApiResponse> findStatus(
        @Validated @RequestBody final KkoPayEltrDocDTO.DocumentBinderUuid reqDTO
    ) {
        return kkoPayEltrcDocService.findStatus(reqDTO);
    }
}
