package kr.xit.core.spring.annotation;

public enum SecurityPolicy {
	/**
	 * 리소스의 아이피, 권한 등 기본 검증 처리
	 */
	DEFAULT,

	/**
	 * 접근한 리소스의 토큰 기반 사용자가 접근할 수 있는 정책 적용
	 */
	TOKEN,

	/**
	 * 접근한 리소스에 세션 기반 인증 사용자가 접근할 수 있는 정책 적용
	 */
	SESSION,

	/**
	 * 접근한 리소스에 쿠키 기반 인증 사용자가 접근할 수 있는 정책 적용
	 */
	COOKIE,

	/**
	 * 보안 검사를 수행하지 않도록 정책 적용
	 */
	NONE
}
