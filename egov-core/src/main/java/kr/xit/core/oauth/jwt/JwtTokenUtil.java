package kr.xit.core.oauth.jwt;// package kr.xit.core.config.auth;
//
// import java.io.Serializable;
// import java.util.Arrays;
// import java.util.Collection;
// import java.util.Date;
// import java.util.function.Function;
// import java.util.stream.Collectors;
//
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
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
// import kr.xit.core.Constants;
// import kr.xit.core.ErrorCode;
// import kr.xit.core.config.properties.JwtTokenProperties;
// import kr.xit.core.exception.BizRuntimeException;
// import kr.xit.core.support.utils.Checks;
// import lombok.extern.slf4j.Slf4j;
//
// @SuppressWarnings("serial")
// @Slf4j
// @Component
// public class JwtTokenUtil implements Serializable {
//
//     @Value("${app.token.secret-key}")
//     private String SIGNING_KEY;
//     private int ACCESS_TOKEN_VALIDITY_SECONDS = 24 * 60 * 60;
//
//     public String getUserIdFromToken(String token) {
//         Claims claims =  getClaimFromToken(token);
//         return claims.get("userId").toString();
//     }
//
//     public String getAdminAuthFromToken(String token) {
//         Claims claims =  getClaimFromToken(token);
//         return claims.get("auth").toString();
//     }
//
//     public Date getExpirationDateFromToken(String token) {
//         return getClaimFromToken(token, Claims::getExpiration);
//     }
//
//     public Claims getClaimFromToken(String token) {
//         final Claims claims = getAllClaimsFromToken(token);
//         return claims;
//     }
//
//
//     public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
//         final Claims claims = getAllClaimsFromToken(token);
//         return claimsResolver.apply(claims);
//     }
//
//     private Claims getAllClaimsFromToken(String token) {
//         try{
//             return Jwts.parser()
//                 .setSigningKey(SIGNING_KEY)
//                 .parseClaimsJws(token)
//                 .getBody();
//         } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
//             log.error(ErrorCode.INVALID_SIGN_TOKEN.getMessage());
//             throw BizRuntimeException.create(ErrorCode.INVALID_SIGN_TOKEN);
//         } catch (ExpiredJwtException e) {
//             log.error(ErrorCode.EXPIRED_TOKEN.getMessage());
//             throw BizRuntimeException.create(ErrorCode.EXPIRED_TOKEN);
//         } catch (UnsupportedJwtException e) {
//             log.error(ErrorCode.INVALID_TOKEN.getMessage());
//             throw BizRuntimeException.create(ErrorCode.INVALID_TOKEN);
//         } catch (IllegalArgumentException e) {
//             log.error("JWT 토큰이 잘못되었습니다.");
//             throw BizRuntimeException.create(ErrorCode.INVALID_TOKEN);
//         }
//     }
//
//     private Boolean isTokenExpired(String token) {
//         final Date expiration = getExpirationDateFromToken(token);
//         if (expiration == null) {
//             return false;
//         }
//         return expiration.before(new Date());
//     }
//
//     public String generateToken(String userId) {
//         return doGenerateToken(userId,"user");
//     }
//
//     private String doGenerateToken(String userId, String subject) {
//
//         Claims claims = Jwts.claims();
//         claims.put("userId", userId);
//         claims.put("auth", "");
//         claims.put("scopes", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
//
//         return Jwts.builder()
//                 .setSubject(subject)
//                 .setClaims(claims)
//                 .setIssuedAt(new Date(System.currentTimeMillis()))
//                 .signWith(SignatureAlgorithm.HS512, SIGNING_KEY)
//                 .compact();
//
//     }
//
//     private String doGenerateTokenForAdmin(String userId, String subject) {
//
//         Claims claims = Jwts.claims();
//         claims.put("userId", userId);
//         claims.put("auth", "AD");
//         claims.put("scopes", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
//
//         return Jwts.builder()
//                 .setSubject(subject)
//                 .setClaims(claims)
//                 .setIssuedAt(new Date(System.currentTimeMillis()))
//                 .signWith(SignatureAlgorithm.HS512, SIGNING_KEY)
//                 .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS*1000))
//                 .compact();
//
//     }
//
//
//
//     public Boolean validateToken(String token, UserDetails userDetails) {
//         final String userId = getUserIdFromToken(token);
//         return (
//                 userId.equals(userDetails.getUsername())
//                         && !isTokenExpired(token));
//     }
//
//      /**
//      * 토큰 유효성체크, 토큰값에 userid가 존재하는지 여부만 체크한다. 만료기한 x
//      * @param token
//      * @return
//      */
//     public Boolean validateToken(String token) {
//         final String userId = getUserIdFromToken(token);
//         return !userId.isEmpty();
//     }
//
//     public Boolean validateTokenForAdmin(String token) {
//         final String userId = getUserIdFromToken(token);
//         return (!userId.isEmpty() && !isTokenExpired(token));
//     }
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
//     // public boolean validateTokenExcludeExpired(String token, boolean isCheckRefresh, boolean isExceptionThrow) {
//     //     try {
//     //         Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//     //         if (!isCheckRefresh && Checks.isEmpty(claimsJws.getBody().getSubject())) {
//     //             if (isExceptionThrow) throw new TokenAuthException(ErrorCode.INVALID_TOKEN);
//     //             return false;
//     //         }
//     //         return true;
//     //
//     //     } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
//     //         log.error(ErrorCode.INVALID_SIGN_TOKEN.getMessage());
//     //         //throw BizRuntimeException.create(ErrorCode.INVALID_SIGN_TOKEN);
//     //     } catch (ExpiredJwtException e) {
//     //         log.error(ErrorCode.EXPIRED_TOKEN.getMessage());
//     //         return true;
//     //     } catch (UnsupportedJwtException e) {
//     //         log.error(ErrorCode.INVALID_TOKEN.getMessage());
//     //         //throw BizRuntimeException.create(ErrorCode.INVALID_TOKEN);
//     //     } catch (IllegalArgumentException e) {
//     //         log.error("JWT 토큰이 잘못되었습니다.");
//     //         //throw BizRuntimeException.create(ErrorCode.INVALID_TOKEN);
//     //     }
//     //
//     //     return false;
//     // }
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
//      * @param token String
//      * @return Authentication
//      */
//     public Authentication getAuthentication(String token) {
//         // 토큰 복호화 : 만료된 토큰이어도 정보를 꺼내기 위해 메소드 분리
//         Claims claims = getAllClaimsFromToken(token);
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
//             Arrays.stream(claims.get(Constants.JwtToken.AUTHORITIES_KEY.getCode()).toString().split(","))
//                 .map(SimpleGrantedAuthority::new)
//                 .collect(Collectors.toList());
//
//         // UserDetails 객체를 만들어서 Authentication 리턴
//         UserDetails principal = new User(claims.getSubject(), "", authorities);
//
//         return new UsernamePasswordAuthenticationToken(principal, "", authorities);
//     }
// }
