package kr.xit.core.api;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <pre>
 * description : Api 응답 인터페이스
 * packageName : kr.xit.core.api
 * fileName    : IRestApiResponse
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
@Schema(required = true, name = "RestApiResponse", description = "Rest Api Response interface class")
public interface IRestApiResponse {
}
