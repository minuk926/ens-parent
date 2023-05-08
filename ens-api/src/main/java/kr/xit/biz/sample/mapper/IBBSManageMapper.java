package kr.xit.biz.sample.mapper;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

import kr.xit.biz.sample.model.BoardVO;

@Mapper
public interface IBBSManageMapper {

    /**
     * 게시물 한 건에 대하여 상세 내용을 조회 한다.
     *
     * @param boardVO
     * @return
     * @throws Exception
     */
    BoardVO selectBoardArticle(BoardVO boardVO);
    /**
     * 조건에 맞는 게시물 목록을 조회 한다.
     *
     * @param boardVO
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    List<BoardVO> selectBoardArticleList(BoardVO boardVO);

    /**
     * 조건에 맞는 게시물 목록에 대한 전체 건수를 조회 한다.
     *
     * @param boardVO
     * @return
     * @throws Exception
     */
    int selectBoardArticleListCnt(BoardVO boardVO);

}
