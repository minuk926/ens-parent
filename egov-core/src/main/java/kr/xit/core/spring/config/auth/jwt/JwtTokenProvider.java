// package kr.xit.core.spring.config.auth.jwt;
//
// import java.security.Key;
// import java.util.Arrays;
// import java.util.Base64;
// import java.util.Collection;
// import java.util.Date;
// import java.util.HashMap;
// import java.util.LinkedHashMap;
// import java.util.Map;
// import java.util.Objects;
// import java.util.stream.Collectors;
//
// import org.springframework.beans.factory.annotation.Value;
// // import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// // import org.springframework.security.core.Authentication;
// // import org.springframework.security.core.GrantedAuthority;
// // import org.springframework.security.core.authority.SimpleGrantedAuthority;
// // import org.springframework.security.core.userdetails.User;
// // import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.stereotype.Component;
//
// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.ExpiredJwtException;
// import io.jsonwebtoken.Jws;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.MalformedJwtException;
// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.UnsupportedJwtException;
// import io.jsonwebtoken.security.Keys;
// import io.swagger.v3.oas.annotations.media.Schema;
// import kr.xit.core.consts.Constants;
// import kr.xit.core.consts.ErrorCode;
// import kr.xit.core.consts.ErrorCode;
// import kr.xit.core.exception.BizRuntimeException;
// import kr.xit.core.spring.config.properties.JwtTokenProperties;
// import kr.xit.core.support.utils.Checks;
// import kr.xit.core.support.utils.DateUtils;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
// import lombok.extern.slf4j.Slf4j;
//
// /**
//  * Token Auth Provider
//  *
//  *
//  */
// /**
//  * <pre>
//  * description : JwtTokenProvider
//  * packageName : kr.xit.core.spring.config.auth.jwt
//  * fileName    : JwtTokenProvider
//  * author      : julim
//  * date        : 2023-04-28
//  * ======================================================================
//  * 변경일         변경자        변경 내용
//  * ----------------------------------------------------------------------
//  * 2023-04-28    julim       최초 생성
//  *
//  * </pre>
//  */
//
// @Slf4j
// @Component
// public class JwtTokenProvider {
//
//     private final Key key;
//     private final JwtTokenProperties jwtTokenProperties;
//
//     private static final long EXPIRE_CONVERT_MIN_TIME = 1000 * 60;                 // 분
//     private static final long EXPIRE_CONVERT_DAY_TIME = 1000 * 60 * 60 * 24;
//
//     /**
//      *
//      * @param secret String
//      */
//     public JwtTokenProvider(@Value("${app.token.secretKey}")String secret, JwtTokenProperties jwtTokenProperties) {
//         this.key = Keys.hmacShaKeyFor(secret.getBytes());
//         //byte[] keyBytes = Decoders.BASE64.decode(secret);
//         //this.key = Keys.hmacShaKeyFor(keyBytes);
//         this.jwtTokenProperties = jwtTokenProperties;
//     }
//
//     /**
//      *
//      * @param authentication Authentication
//      * @return String
//      */
//     public String generateJwtAccessToken(String authentication) {
//         return getJwtAccessToken(authentication, null);
//     }
//
//     /**
//      *
//      * @param authentication Authentication
//      * @return String
//      */
//     public String generateJwtAccessToken(Authentication authentication, Map<String, Object> infoMap) {
//         return getJwtAccessToken(authentication, infoMap);
//     }
//
//     public String generateJwtAccessToken(String userId, String authorities) {
//         return getJwtAccessToken(userId, authorities, null);
//     }
//
//     /**
//      *
//      * @param authentication Authentication
//      * @return String
//      */
//     public String generateJwtRefreshToken(Authentication authentication) {
//         return getJwtRefreshToken(authentication.getName());
//     }
//
//     public String generateJwtRefreshToken(String userId) {
//         return getJwtRefreshToken(userId);
//     }
//
//     /**
//      * token 정보 GET
//      * SecurityContext에 인증 정보를 저장하기위해 Authentication 객체로 return
//      * Access token만 사용자 정보를 담고 있어 Access token만 파라메터로 받는다
//      * <p>
//      * 1. JWT 토큰을 복호화
//      * 3. Authentication GET
//      * 4. UsernamePasswordAuthenticationToken(Authentication) return
//      *
//      * @param accessToken String
//      * @return Authentication
//      */
//     public Authentication getAuthentication(String accessToken) {
//         // 토큰 복호화 : 만료된 토큰이어도 정보를 꺼내기 위해 메소드 분리
//         Claims claims = parseClaims(accessToken);
//
//         if (Checks.isEmpty(claims.getSubject())) {
//             throw BizRuntimeException.create(ErrorCode.INVALID_TOKEN);
//         }
//
//         if (claims.get(Constants.JwtToken.AUTHORITIES_KEY.getCode()) == null) {
//             throw BizRuntimeException.create(ErrorCode.INVALID_ROLE_TOKEN);
//         }
//
//         // 클레임에서 권한 정보 가져오기
//         Collection<? extends GrantedAuthority> authorities =
//                 Arrays.stream(claims.get(Constants.JwtToken.AUTHORITIES_KEY.getCode()).toString().split(","))
//                         .map(SimpleGrantedAuthority::new)
//                         .collect(Collectors.toList());
//
//         // UserDetails 객체를 만들어서 Authentication 리턴
//         UserDetails principal = new User(claims.getSubject(), "", authorities);
//
//         return new UsernamePasswordAuthenticationToken(principal, "", authorities);
//     }
//
//     /**
//      * 토큰 유효기간만 검증
//      * validateTokenExcludeExpired 로 검증한 토큰에 한해서 체크 해야함
//      * @param token
//      * @return
//      */
//     public boolean isExpiredToken(String token) {
//         try {
//             Jwts.parserBuilder()
//                     .setSigningKey(key)
//                     .build()
//                     .parseClaimsJws(token)
//                     .getBody();
//         } catch (ExpiredJwtException e) {
//             log.info("Expired JWT token.");
//             //return e.getClaims();
//             return true;
//         //} catch (Exception e){
//         //    return false;
//         }
//         return false;
//     }
//
//
//     /**
//      * 토큰 정보 검증 - 토큰 만료일은 검증에서 제외
//      * 에러발생시 Jwts 모듈이 알아서 Exception throws
//      * isExceptionThrow = false 인 경우 오염된 토큰인지 체크됨
//      *
//      * @param token String
//      * @param isCheckRefresh refresh token인 경우 true
//      * @param isExceptionThrow 검증 오류시 Exception을 throw 할 경우 true
//      * @return boolean
//      */
//     public boolean validateTokenExcludeExpired(String token, boolean isCheckRefresh, boolean isExceptionThrow) {
//         try {
//             Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//             if (!isCheckRefresh && Checks.isEmpty(claimsJws.getBody().getSubject())) {
//                 if (isExceptionThrow) throw BizRuntimeException.create(ErrorCode.INVALID_TOKEN);
//                 return false;
//             }
//             return true;
//         } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
//             log.info("잘못된 JWT 서명입니다.");
//             if (isExceptionThrow) throw BizRuntimeException.create(ErrorCode.INVALID_SIGN_TOKEN);
//         } catch (ExpiredJwtException e) {
//             log.info("만료된 JWT 토큰입니다.");
//             return true;
//             //if (isExceptionThrow) throw new JwtTokenExpiredException(ErrorCode.EXPIRED_TOKEN);
//         } catch (UnsupportedJwtException e) {
//             log.info("지원되지 않는 JWT 토큰입니다.");
//             if (isExceptionThrow) throw BizRuntimeException.create(ErrorCode.INVALID_TOKEN);
//         } catch (IllegalArgumentException e) {
//             log.info("JWT 토큰이 잘못되었습니다.");
//             if (isExceptionThrow) throw BizRuntimeException.create(ErrorCode.INVALID_TOKEN);
//         }
//         return false;
//     }
//
//     /**
//      * authentication 정보로 Access token과 Refresh token 생성
//      * Access token에는 사용자와 권한 정보를 담고, Refresh token에는 만료일자만 담는다
//      *
//      * @param authentication Authentication
//      * @return TokenDto
//      */
//     public TokenDto generateTokenDto(Authentication authentication, Map<String, Object> info) {
//         // Access Token 생성
//         String accessToken = getJwtAccessToken(authentication, info);
//
//         // Refresh Token 생성
//         String refreshToken = getJwtRefreshToken(authentication.getName());
//
//         return TokenDto.builder()
//                 .grantType(Constants.JwtToken.GRANT_TYPE.getCode())
//                 .accessToken(accessToken)
//                 .refreshToken(refreshToken)
//                 .build();
//     }
//
//     public Map<String, Object> getAccessTokenInfo(String accessToken) {
//         Map<String, Object> map = new LinkedHashMap<>();
//
//         // 토큰 복호화 : 만료된 토큰이어도 정보를 꺼내기 위해 메소드 분리
//         Claims claims = parseClaims(accessToken);
//
//         Collection<? extends GrantedAuthority> authorities = null;
//
//         if (claims.get(Constants.JwtToken.AUTHORITIES_KEY.getCode()) != null) {
//             // 클레임에서 권한 정보 가져오기
//             authorities =
//                     Arrays.stream(claims.get(Constants.JwtToken.AUTHORITIES_KEY.getCode()).toString().split(","))
//                             .map(SimpleGrantedAuthority::new)
//                             .collect(Collectors.toList());
//         }
//
//         if (authorities != null) {
//             //map.put("header", claims)
//             map.put("audience", claims.getAudience());
//             map.put(Constants.JwtToken.TOKEN_USER_ID.getCode(), claims.getSubject());
//             map.put(Constants.JwtToken.TOKEN_USER_NAME.getCode(), claims.get(Constants.JwtToken.TOKEN_USER_NAME.getCode()));
//             map.put(Constants.JwtToken.TOKEN_USER_MAIL.getCode(), claims.get(Constants.JwtToken.TOKEN_USER_MAIL.getCode()));
//             map.put(Constants.JwtToken.AUTHORITIES_KEY.getCode(), authorities);
//             map.put("Issuer", claims.getIssuer());
//             map.put("IssuedAt", DateUtils.getFormatedDT(claims.getIssuedAt(), "yyyy-MM-dd HH:mm:ss"));
//         } else {
//             map.put("Refresh token", "Refresh token 입니다.");
//         }
//         map.put("expiration", DateUtils.getFormatedDT(claims.getExpiration(), "yyyy-MM-dd HH:mm:ss"));
//
//         return map;
//     }
//
//
//     private String getJwtAccessToken(Authentication authentication, Map<String, Object> info) {
//         // 권한들 가져오기
//         String authorities = authentication.getAuthorities().stream()
//                 .map(GrantedAuthority::getAuthority)
//                 .collect(Collectors.joining(","));
//
//         return getJwtAccessToken(authentication.getName(), authorities, info);
//     }
//
//     private String getJwtAccessToken(String userId, String authorities, Map<String, Object> info) {
//         long now = (new Date()).getTime();
//         Date accessTokenExpiresIn = new Date(now + (jwtTokenProperties.getTokenExpiry() * EXPIRE_CONVERT_MIN_TIME));
//
//         Map<String, Object> header = new HashMap<>();
//         header.put("typ", jwtTokenProperties.getTyp());
//         header.put("alg", jwtTokenProperties.getAlg());
//
//         // Access Token 생성
//         if(Checks.isNotEmpty(info)) {
//             return Jwts.builder()
//                     .setHeader(header)
//                     .setIssuer(jwtTokenProperties.getIssure())
//                     .setAudience(jwtTokenProperties.getAudience())
//                     .setSubject(userId)                                   // payload "sub": "name" - ID
//                     .claim(Constants.JwtToken.AUTHORITIES_KEY.getCode(), authorities)                                    // payload "auth": "ROLE_USER"
//                     .claim(Constants.JwtToken.TOKEN_USER_NAME.getCode(), info.get(Constants.JwtToken.TOKEN_USER_NAME.getCode()))
//                     .claim(Constants.JwtToken.TOKEN_USER_MAIL.getCode(), info.get(Constants.JwtToken.TOKEN_USER_MAIL.getCode()))
//                     .setIssuedAt(new Date())
//                     .signWith(key, Objects.equals("HS512", jwtTokenProperties.getAlg())? SignatureAlgorithm.HS512 : SignatureAlgorithm.HS256)                                // header "alg": "HS512"
//                     .setExpiration(accessTokenExpiresIn)      // payload "exp": 1516239022 (예시)
//                     .compact();
//         }else{
//             return Jwts.builder()
//                     .setHeader(header)
//                     .setIssuer(jwtTokenProperties.getIssure())
//                     .setAudience(jwtTokenProperties.getAudience())
//                     .setSubject(userId)                                   // payload "sub": "name" - ID
//                     .claim(Constants.JwtToken.AUTHORITIES_KEY.getCode(), authorities)                                    // payload "auth": "ROLE_USER"
//                     .setIssuedAt(new Date())
//                     .signWith(key, Objects.equals("HS512", jwtTokenProperties.getAlg())? SignatureAlgorithm.HS512 : SignatureAlgorithm.HS256)                          // header "alg": "HS512"
//                     .setExpiration(accessTokenExpiresIn)      // payload "exp": 1516239022 (예시)
//                     .compact();
//         }
//     }
//
//     private String getJwtRefreshToken(String userId) {
//         long now = (new Date()).getTime();
//
//         // Refresh Token 생성
//         return Jwts.builder()
//                 .setSubject(userId)
//                 .signWith(key, Objects.equals("HS512", jwtTokenProperties.getAlg())? SignatureAlgorithm.HS512 : SignatureAlgorithm.HS256)
//                 .setExpiration(new Date(now + (jwtTokenProperties.getRefreshTokenExpiry() * EXPIRE_CONVERT_DAY_TIME)))
//                 .compact();
//     }
//
// //    public boolean validate(String accessToken) {
// //        return parseClaims(accessToken) != null;
// //    }
//
//     public Claims parseClaims(String token) {
//         try {
//             return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
//         } catch (ExpiredJwtException e) {
//             return e.getClaims();
//         }
//     }
//
//     public UsernamePasswordAuthenticationToken toAuthentication(String userId, String passwd) {
//         return new UsernamePasswordAuthenticationToken(userId, passwd);
//     }
//
//     public static void main(String[] args) {
//
//         String tgt = "spring-boot-security-jwt-xit-core-javaframework-java-token-key";
//
//         String encodedString = Base64.getEncoder().withoutPadding().encodeToString(tgt.getBytes());
//         log.debug("\ntarget bit={}\nOriginal={}\nBase64 Encoding={}\nBase64 Decoding={}", tgt.length()*8, tgt, encodedString, new String(Base64.getDecoder().decode(encodedString)));
//     }
//
//
//     @Schema(name = "TokenDto", description = "토큰 정보")
//     @Getter
//     @Setter
//     @NoArgsConstructor
//     @AllArgsConstructor
//     @Builder
//     public static class TokenDto {
//
//         @Schema(required = true, title = "토큰타입", example = "Bearer", description = "토큰 타입")
//         private String grantType;
//
//         @Schema(required = true, title = "Access token", example = " ", description = "Access token")
//         private String accessToken;
//
//         @Schema(required = true, title = "Refresh token", example = " ", description = "Refresh token")
//         private String refreshToken;
//
//         //@Schema(required = true, title = "User info", example = " ", description = "User info")
//         //private MinUserinfoDto user;
//     }
// }
