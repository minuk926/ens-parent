package egovframework.let.uat.uia.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.jwt.config.EgovJwtTokenUtil;
import egovframework.let.uat.uia.service.EgovLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.xit.core.Constants;
import kr.xit.core.api.RestApiResponse;
import kr.xit.core.api.IRestApiResponse;
import kr.xit.core.oauth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
//import kr.xit.core.config.auth.JwtTokenUtil;

/**
 * 일반 로그인을 처리하는 컨트롤러 클래스
 * @author 공통서비스 개발팀 박지욱
 * @since 2009.03.06
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일      수정자      수정내용
 *  -------            --------        ---------------------------
 *  2009.03.06  박지욱     최초 생성
 *  2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 *  </pre>
 */
@Tag(name = "EgovLoginApiController", description = "인증 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/cmm/uat/auth")
public class EgovLoginApiController {
	@Value("${app.token.saveType:header}")
	private String authSaveType;

	/** EgovLoginService */

	private final EgovLoginService loginService;

	/** EgovMessageSource */

	private final EgovMessageSource egovMessageSource;

	/** EgovPropertyService */

	private final EgovPropertyService propertiesService;
	
	/** JWT */
    private final EgovJwtTokenUtil egovJwtTokenUtil;
	private final JwtTokenProvider jwtTokenProvider;

	/**
	 * 일반 로그인을 처리한다
	 * @param loginVO - 아이디, 비밀번호가 담긴 LoginVO
	 * @param request - 세션처리를 위한 HttpServletRequest
	 * @return result - 로그인결과(세션정보)
	 * @exception Exception
	 */
	@PostMapping(value = "/actionLoginAPI.do", consumes = {MediaType.APPLICATION_JSON_VALUE , MediaType.TEXT_HTML_VALUE})
	public ResponseEntity<? extends IRestApiResponse> actionLogin(@RequestBody LoginVO loginVO, HttpServletRequest request) throws Exception {
		HashMap<String,Object> resultMap = new HashMap<String,Object>();

		// 1. 일반 로그인 처리
		LoginVO loginResultVO = loginService.actionLogin(loginVO);

		if (loginResultVO != null && loginResultVO.getId() != null && !loginResultVO.getId().equals("")) {
			return RestApiResponse.of(loginResultVO);
		}
		return RestApiResponse.of("300", egovMessageSource.getMessage("fail.common.login"));
	}

	@Operation(summary = "로그인(토큰)" , description = "로그인(토큰)")
	@PostMapping(value = "/actionLoginJWT.do")
	public ResponseEntity<? extends IRestApiResponse> actionLoginJWT(@RequestBody LoginVO loginVO, HttpServletRequest request, HttpSession session) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		// 1. 일반 로그인 처리
		LoginVO loginResultVO = loginService.actionLogin(loginVO);
		
		if (loginResultVO != null && loginResultVO.getId() != null && !loginResultVO.getId().equals("")) {

			System.out.println("===>>> loginVO.getUserSe() = "+loginVO.getUserSe());
			System.out.println("===>>> loginVO.getId() = "+loginVO.getId());
			System.out.println("===>>> loginVO.getPassword() = "+loginVO.getPassword());
			
	//		String jwtToken = egovJwtTokenUtil.generateToken(loginVO);
	//		String jwtToken = egovJwtTokenUtil.generateToken(loginVO.getId());

		//	String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
	    //	System.out.println("Dec jwtToken username = "+username);
	    	 
	    	//서버사이드 권한 체크 통과를 위해 삽입
	    	//EgovUserDetailsHelper.isAuthenticated() 가 그 역할 수행. DB에 정보가 없으면 403을 돌려 줌. 로그인으로 튕기는 건 프론트 쪽에서 처리
	    	//request.getSession().setAttribute("LoginVO", loginResultVO);




			// UsernamePasswordAuthenticationToken authenticationToken = jwtTokenProvider.toAuthentication(loginVO.getId(), loginVO.getPassword());
			// Authentication authentication = authenticationManager.authenticate(authenticationToken);
			//
			//
			// // Authentication 저장
			// if(Objects.equals(authSaveType, Constants.AuthSaveType.SECURITY.getCode())){
			// 	// TODO :: SessionCreationPolicy.STATELESS 인 경우 사용 불가
			// 	SecurityContextHolder.getContext().setAuthentication(authentication);
			//
			// }else if(Objects.equals(authSaveType, Constants.AuthSaveType.SESSION.getCode())){
			// 	session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
			// }

			Map<String, Object> infoMap = new HashMap<>();
			infoMap.put(Constants.JwtToken.TOKEN_USER_ID.getCode(), loginVO.getId());
			infoMap.put(Constants.JwtToken.TOKEN_USER_MAIL.getCode(), loginVO.getEmail());

			//String jwtToken = jwtTokenProvider.generateJwtAccessToken(authentication, infoMap);
			String jwtToken = jwtTokenProvider.generateJwtAccessToken(loginVO.getId(), "ROLE_USER");

			resultMap.put("resultVO", loginResultVO);
			resultMap.put("token", jwtToken);
		    return RestApiResponse.of(resultMap);
			
		}
		return RestApiResponse.of("300", egovMessageSource.getMessage("fail.common.login") );
	}

	/**
	 * 로그아웃한다.
	 * @return resultVO
	 * @exception Exception
	 */
	@GetMapping(value = "/actionLogoutAPI.do")
	public  ResponseEntity<? extends IRestApiResponse> actionLogoutJSON(HttpServletRequest request) throws Exception {
		ResultVO resultVO = new ResultVO();

		RequestContextHolder.currentRequestAttributes().removeAttribute("LoginVO", RequestAttributes.SCOPE_SESSION);

		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());

		return RestApiResponse.of();
	}
}
