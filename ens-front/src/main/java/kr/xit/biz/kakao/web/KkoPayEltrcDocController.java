package kr.xit.biz.kakao.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
@Tag(name = "KkoPayEltrcDocController", description = "카카오페이 전자문서 발송 요청")
@RestController
@RequestMapping(value = "/api/kakaopay/test")
public class KkoPayEltrcDocController {

    @GetMapping
    public ModelAndView kakaopay(){
        ModelAndView mav = new ModelAndView("th/kakaopay");
        return mav;
    }

}
