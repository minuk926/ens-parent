package kr.xit.ens.support.kakao.web;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.xit.core.api.IRestApiResponse;
import kr.xit.core.api.RestApiErrorResponse;
import kr.xit.core.api.RestApiResponse;
import kr.xit.ens.support.kakao.dto.KkoPayEltrDocDTO;
import kr.xit.ens.support.kakao.service.IKkoPayEltrcDocService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "KakaoController")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/kakaopay/documents")
public class KkoPayEltrcController {

    private final IKkoPayEltrcDocService kkoPayEltrcDocService;


    @Operation(summary = "문서발송 요청", description = "카카오페이 전자문서 서버로 문서발송 처리를 요청")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends IRestApiResponse> requestSend(
        @Validated @RequestBody KkoPayEltrDocDTO.RequestSendReq reqDTO
    ) {
        return kkoPayEltrcDocService.requestSend(reqDTO);
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
