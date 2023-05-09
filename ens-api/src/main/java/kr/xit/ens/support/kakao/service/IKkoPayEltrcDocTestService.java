package kr.xit.ens.support.kakao.service;

import kr.xit.core.api.IRestApiResponse;
import kr.xit.ens.support.kakao.model.KkoPayEltrDocDTO;
import org.springframework.http.ResponseEntity;

/**
 * <pre>
 * description : 카카오 페이 전자 문서 발송 요청 테스트 인터 페이스
 * packageName : kr.xit.ens.support.kakao.service
 * fileName    : IKkoPayEltrcDocTestService
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
public interface IKkoPayEltrcDocTestService {
    ResponseEntity<? extends IRestApiResponse> dummy();

    /**
     * <pre>
     * 모바일웹 연계 문서발송 요청
     * -.이용기관 서버에서 전자문서 서버로 문서발송 처리를 요청합니다.
     * </pre>
     * @param reqDTO
     * @return ResponseEntity
     */
    ResponseEntity<? extends IRestApiResponse> requestSend(final KkoPayEltrDocDTO.RequestSendReq reqDTO);


    /**
     * <pre>
     * 토큰 유효성 검증(Redirect URL  접속 허용/불허)
     * </pre>
     * @param reqDTO KkoPayEltrDocDTO.RequestSendReq
     * @return ResponseEntity
     */
    ResponseEntity<? extends IRestApiResponse> validToken(final KkoPayEltrDocDTO.ValidTokenReq reqDTO);

}

