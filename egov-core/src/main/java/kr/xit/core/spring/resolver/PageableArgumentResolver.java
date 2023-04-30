package kr.xit.core.spring.resolver;

import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * description :  Dispatchersevlet 이 요청 전달시 컨트롤러에서 필요로 하는 객체 생성 및 바인딩
 *                아래의 어노테이션이 ArgumentResolver로 동작
 *                * @RequestParam: 쿼리 파라미터 값 바인딩
 *                * @ModelAttribute: 쿼리 파라미터 및 폼 데이터 바인딩
 *                * @CookieValue: 쿠키값 바인딩
 *                * @RequestHeader: 헤더값 바인딩
 *                * @RequestBody: 바디값 바인딩
 * packageName : kr.xit.core.spring.resolver
 * fileName    : PageableArgumentResolver
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see HandlerMethodArgumentResolver
 */
// FIXME:: PageableArgumentResolver
@Slf4j
public class PageableArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 파라미터가 Pageable 타입이면 지원한다
        return PaginationInfo.class.equals(parameter.getParameterType());

        // PaginationInfo paginationInfo = new PaginationInfo();
        // paginationInfo.setCurrentPageNo(boardVO.getPageIndex());
        // paginationInfo.setRecordCountPerPage(propertyService.getInt("pageUnit"));
        // paginationInfo.setPageSize(propertyService.getInt("pageSize"));
        //
        // boardVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        // boardVO.setLastIndex(paginationInfo.getLastRecordIndex());
        // boardVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {


        //return new PageRequest(offset / limit, limit, new Sort(Sort.Direction.DESC, "seq"));
        return null;
    }
}
