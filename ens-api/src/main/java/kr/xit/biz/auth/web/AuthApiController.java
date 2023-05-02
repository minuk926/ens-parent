package kr.xit.biz.auth.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import egovframework.com.cmm.jwt.config.EgovJwtTokenUtil;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import kr.xit.biz.auth.service.AuthApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.xit.core.consts.Constants;
import kr.xit.core.api.IRestApiResponse;
import kr.xit.core.api.RestApiResponse;
//import kr.xit.core.spring.config.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * description : 인증 로그인 처리
 *
 * packageName : kr.xit.biz.auth
 * fileName    : AuthApiController
 * author      : limju
 * date        : 2023-04-26
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-26    limju       최초 생성
 *
 * </pre>
 * @see
 */
//FIXME::세션에서 인증 관리 할지 여부 결정 필요
@Tag(name = "AuthApiController", description = "인증 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthApiController {
	@Value("${app.token.saveType:header}")
	private String authSaveType;

	/** EgovLoginService */

	private final AuthApiService loginService;

	/** EgovMessageSource */

	private final EgovMessageSource egovMessageSource;
	private final EgovPropertyService propertiesService;
	//private final JwtTokenProvider jwtTokenProvider;
	private final EgovJwtTokenUtil egovJwtTokenUtil;

	/**
	 * 일반 로그인을 처리한다
	 * @param loginVO 아이디, 비밀번호가 담긴 LoginVO
	 * @param request 세션처리를 위한 HttpServletRequest
	 * @return 로그인결과(세션정보)
	 */
	@Operation(summary = "로그인" , description = "로그인")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
		required = true,
		content = {
			@Content(
				mediaType = "application/json",
				examples = {
					@ExampleObject(
						value = "{\"id\":\"admin\",\"password\":\"1\",\"userSe\":\"USR\"}")
				}
			)
		}
	)
	@PostMapping(value = "/login", consumes = {MediaType.APPLICATION_JSON_VALUE , MediaType.TEXT_HTML_VALUE})
	public ResponseEntity<? extends IRestApiResponse> login(@RequestBody final LoginVO loginVO, HttpServletRequest request) {
		// 1. 일반 로그인 처리
		LoginVO loginResultVO = loginService.actionLogin(loginVO);

		if (loginResultVO != null && loginResultVO.getId() != null && !loginResultVO.getId().equals("")) {
			request.getSession().setAttribute(Constants.AuthSaveSession.LOGIN_VO.getCode(), loginResultVO);
			return RestApiResponse.of(loginResultVO);
		}
		return RestApiResponse.of("300", egovMessageSource.getMessage("fail.common.login"));
	}

	@Operation(summary = "로그인(JWT)" , description = "로그인(JWT)")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
		required = true,
		content = {
			@Content(
				mediaType = "application/json",
				examples = {
					@ExampleObject(
						name = "admin",
						description = "admin",
						value = "{\"id\":\"admin\",\"password\":\"1\",\"userSe\":\"USR\"}"),
					@ExampleObject(
						name = "admin1",
						description = "admin1",
						value = "{\"id\":\"admin1\",\"password\":\"1\",\"userSe\":\"USR\"}"),
					@ExampleObject(
						name = "admin2",
						description = "admin2",
						value = "{\"id\":\"admin2\",\"password\":\"1\",\"userSe\":\"USR\"}")
				}
			)
		}
	)
	@PostMapping(value = "/loginJwt")
	public ResponseEntity<? extends IRestApiResponse> loginJWT(@RequestBody final LoginVO loginVO, HttpServletRequest request) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		// 1. 일반 로그인 처리
		LoginVO loginResultVO = loginService.actionLogin(loginVO);
		
		if (loginResultVO != null && loginResultVO.getId() != null && !loginResultVO.getId().equals("")) {

			System.out.println("===>>> loginVO.getUserSe() = "+loginVO.getUserSe());
			System.out.println("===>>> loginVO.getId() = "+loginVO.getId());
			System.out.println("===>>> loginVO.getPassword() = "+loginVO.getPassword());
			
			String jwtToken = egovJwtTokenUtil.generateToken(loginVO);
	//		String jwtToken = egovJwtTokenUtil.generateToken(loginVO.getId());

			String username = egovJwtTokenUtil.getUsernameFromToken(jwtToken);
	    //	System.out.println("Dec jwtToken username = "+username);
	    	 
	    	//서버사이드 권한 체크 통과를 위해 삽입
	    	//EgovUserDetailsHelper.isAuthenticated() 가 그 역할 수행. DB에 정보가 없으면 403을 돌려 줌. 로그인으로 튕기는 건 프론트 쪽에서 처리
	    	request.getSession().setAttribute(Constants.AuthSaveSession.LOGIN_VO.getCode(), loginResultVO);




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

			//Map<String, Object> infoMap = new HashMap<>();
			//infoMap.put(Constants.JwtToken.TOKEN_USER_ID.getCode(), loginVO.getId());
			//infoMap.put(Constants.JwtToken.TOKEN_USER_MAIL.getCode(), loginVO.getEmail());

			//String jwtToken = jwtTokenProvider.generateJwtAccessToken(authentication, infoMap);
			//String jwtToken = jwtTokenProvider.generateJwtAccessToken(loginVO.getId(), "ROLE_USER");

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
	@Operation(summary = "logout" , description = "로그아웃")
	@GetMapping(value = "/logout")
	public  ResponseEntity<? extends IRestApiResponse> actionLogoutJSON(HttpServletRequest request) throws Exception {

		RequestContextHolder.currentRequestAttributes().removeAttribute(Constants.AuthSaveSession.LOGIN_VO.getCode(), RequestAttributes.SCOPE_SESSION);
		return RestApiResponse.of();
	}
}
