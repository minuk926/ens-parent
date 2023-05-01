package kr.xit.biz.sample.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.xit.biz.sample.dto.BoardVO;
import kr.xit.biz.sample.service.EgovBBSManageService;
import kr.xit.core.api.IRestApiResponse;
import kr.xit.core.api.RestApiResponse;
import kr.xit.core.exception.BizRuntimeException;
import kr.xit.core.spring.annotation.Secured;
import kr.xit.core.spring.annotation.SecurityPolicy;
import lombok.RequiredArgsConstructor;

//@SessionAttributes(types = ComDefaultVO.class)


@Tag(name = "SampleController", description = "Api 예제")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/sample")
public class SampleController {

    private final EgovBBSManageService bbsMngService;

	@Operation(summary = "게시판 조회" , description = "게시판 조회")
	@PostMapping(value = "/main")
	public ResponseEntity<? extends IRestApiResponse> findMain(){
		return RestApiResponse.of(getMain());
	}


	@Operation(summary = "게시판 조회-인증사용자" , description = "게시판 조회-인증사용자")
	@Secured(policy = SecurityPolicy.TOKEN)
	@PostMapping(value = "/main2")
	public ResponseEntity<? extends IRestApiResponse> findMain2() {

		return RestApiResponse.of(getMain());
	}

	private Map<String, Object> getMain(){

		//ResultVO resultVO = new ResultVO();
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


		return resultMap;
	}
}
