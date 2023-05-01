package kr.xit.biz.sample.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.util.EgovDateUtil;
import kr.xit.biz.sample.dto.BoardVO;
import kr.xit.biz.sample.mapper.IBBSManageMapper;
import kr.xit.biz.sample.service.EgovBBSManageService;

/**
 * 게시물 관리를 위한 서비스 구현 클래스
 * @author 공통 서비스 개발팀 한성곤
 * @since 2009.03.19
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2009.03.19  이삼섭          최초 생성
 *  2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 *  </pre>
 */
@Service("EgovBBSManageService")
public class EgovBBSManageServiceImpl extends EgovAbstractServiceImpl implements EgovBBSManageService {

	@Resource
	private IBBSManageMapper ibbsManageMapper;

	@Resource
	protected EgovPropertyService propertyService;

	/**
	 * 조건에 맞는 게시물 목록을 조회 한다.
	 *
	 * @see egovframework.let.cop.bbs.brd.service.EgovBBSManageService#selectBoardArticles(egovframework.let.cop.bbs.brd.service.BoardVO)
	 */
	@Override
	public Map<String, Object> selectBoardArticles(BoardVO boardVO, String attrbFlag) {
		List<BoardVO> list = ibbsManageMapper.selectBoardArticleList(boardVO);
		List<BoardVO> result = new ArrayList<BoardVO>();

		if ("BBSA01".equals(attrbFlag)) {
			// 유효게시판 임
			String today = EgovDateUtil.getToday();

			BoardVO vo;
			Iterator<BoardVO> iter = list.iterator();
			while (iter.hasNext()) {
				vo = iter.next();

				if (!"".equals(vo.getNtceBgnde()) || !"".equals(vo.getNtceEndde())) {
					if (EgovDateUtil.getDaysDiff(today, vo.getNtceBgnde()) > 0
						|| EgovDateUtil.getDaysDiff(today, vo.getNtceEndde()) < 0) {
						// 시작일이 오늘날짜보다 크거나, 종료일이 오늘 날짜보다 작은 경우
						vo.setIsExpired("Y");
					}
				}
				result.add(vo);
			}

		} else {
			result = list;
		}

		int cnt = ibbsManageMapper.selectBoardArticleListCnt(boardVO);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("resultList", result);
		map.put("resultCnt", Integer.toString(cnt));
		//throw BizRuntimeException.create("서비스 에러 테스트");

		return map;
	}
}
