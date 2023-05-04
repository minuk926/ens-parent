package kr.xit.ens.support.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public enum StatCd {

	accept("접수")

	, makerdy("제작준비")
	, making("제작중")
	, makeok("제작성공")
	, makefail("제작실패")

	, sendrdy("전송준비")
	, sending("전송중")
	, sendok("전송성공")
	, sendfail("전송실패")

	, sendcmplt("전송완료")

	, open("열람중")

	, close("조회기간마감")
	;
	
	@Getter
	private String code;

	StatCd(String code){
		this.code = code;
	}
}
