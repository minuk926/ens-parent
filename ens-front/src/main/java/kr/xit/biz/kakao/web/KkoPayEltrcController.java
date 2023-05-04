package kr.xit.biz.kakao.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <pre>
 * description : 카카오페이 전자문서 발송 요청 controller
 *
 * packageName : kr.xit.biz.kakao.web
 * fileName    : KkoPayEltrcController
 * author      : xitdev
 * date        : 2023-05-02
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-05-02    xitdev       최초 생성
 *
 * </pre>
 */
@Tag(name = "KkoPayEltrcController", description = "카카오페이 전자문서 발송 요청")
@RestController
@RequestMapping(value = "/api/v1/documents")
public class KkoPayEltrcController {

    @Operation(summary = "문서발송 요청", description = "카카오페이 전자문서 서버로 문서발송 처리를 요청")
    @GetMapping(value = "/")
    public ModelAndView requestSend(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("th/requestSend");
        return mav;
    }

}
