package egovframework.let.main.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.com.cmm.service.ResultVO;
import egovframework.let.cop.bbs.service.BoardVO;
import egovframework.let.cop.bbs.service.EgovBBSManageService;
import kr.xit.core.api.RestApiResponse;
import kr.xit.core.api.IRestApiResponse;
import kr.xit.core.exception.BizRuntimeException;
import kr.xit.core.spring.annotation.Secured;
import kr.xit.core.spring.annotation.SecurityPolicy;

/**
 * 템플릿 메인 페이지 컨트롤러 클래스(Sample 소스)
 * @author 실행환경 개발팀 JJY
 * @since 2011.08.31
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.08.31  JJY            최초 생성
 *
 * </pre>
 */
@RestController
//@SessionAttributes(types = ComDefaultVO.class)
//@Secured(policy = SecurityPolicy.TOKEN)
public class EgovMainApiController {

	/**
	 * EgovBBSManageService
	 */
	@Resource(name = "EgovBBSManageService")
    private EgovBBSManageService bbsMngService;

	/**
	 * 템플릿 메인 페이지 조회
	 * @return 메인페이지 정보 Map [key : 항목명]
	 *
	 * @throws Exception
	 */
	@PostMapping(value = "/cmm/main/mainPageAPI.do")
	public ResponseEntity<? extends IRestApiResponse> getMgtMainPage(){

		ResultVO resultVO = new ResultVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 공지사항 메인 컨텐츠 조회 시작 ---------------------------------
		BoardVO boardVO = new BoardVO();
		boardVO.setPageUnit(5);
		boardVO.setPageSize(10);
		boardVO.setBbsId("BBSMSTR_00001");

		PaginationInfo paginationInfo = new PaginationInfo();

		paginationInfo.setCurrentPageNo(boardVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(boardVO.getPageUnit());
		paginationInfo.setPageSize(boardVO.getPageSize());

		boardVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		boardVO.setLastIndex(paginationInfo.getLastRecordIndex());
		boardVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		Map<String, Object> map = bbsMngService.selectBoardArticles(boardVO, "BBSA02");
		resultMap.put("notiList", map.get("resultList"));

		map = bbsMngService.selectBoardArticles(boardVO, "BBSA02");
		resultMap.put("galList", map.get("resultList"));

		
		return RestApiResponse.of(resultMap);
		// resultVO.setResult(resultMap);
		// resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		// resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
		//
		// return resultVO;
	}


	@PostMapping(value = "/cmm/main/mainPageAPI2.do")
	// public IApiResult getMgtMainPage()
	public ResponseEntity<? extends IRestApiResponse> getMgtMainPage2() {
		// throw new BaseRuntimeException("dkdkkdkdkkd");
		throw BizRuntimeException.create("dkdkkdkdkkd");

	}
}
