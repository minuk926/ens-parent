package egovframework.com.jwt.config;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import egovframework.com.cmm.LoginVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import kr.xit.core.exception.BizRuntimeException;
import lombok.Setter;

//security 관련 제외한 jwt util 클래스

@Component
@ConfigurationProperties(prefix = "app.token")
public class EgovJwtTokenUtil implements Serializable{

	private static final long serialVersionUID = -5180902194184255251L;
	//public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60; //하루
	public static final long JWT_TOKEN_VALIDITY = (long) ((1 * 60 * 60) / 60) * 60; //토큰의 유효시간 설정, 기본 60분
	
//	@Value("egovframe")
//    private String secret;

    @Setter
    private String typ;
    @Setter
    private String alg;
    @Setter
    private String issuer;
    @Setter
    private String apiKey;
    @Setter
    private String secretKey;

	
	//retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);

        return claimsResolver.apply(claims);
    }
	
    //for retrieveing any information from token we will need the secret key
    public Claims getAllClaimsFromToken(String token) {
    	System.out.println("EgovJwtTokenUtil===>>> secret = "+secretKey);
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes("UTF-8"));
            Jws<Claims> jwsClaims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

            // Claims jwsClaims2 = Jwts.parserBuilder()
            //     .setSigningKey(key)
            //     .build()
            //     .parseClaimsJws(token)
            //     .getBody();

            if(jwsClaims.getBody() != null) {
                Claims claims = jwsClaims.getBody();

                // if (claims.getExpiration().before(new Date()))
                //     throw BizRuntimeException.create("만료된 토큰 입니다");

                return claims;
            }
        } catch (UnsupportedEncodingException e) {
            throw BizRuntimeException.create(e.getMessage());
        } catch (final IllegalArgumentException | MalformedJwtException e) {	// 4
            throw BizRuntimeException.create(e.getMessage());
        } catch (final SignatureException e) {	// 5
            throw BizRuntimeException.create(e.getMessage());
        } catch (final ExpiredJwtException e) {	// 6
            throw  BizRuntimeException.create(e.getMessage());
        }
        return null;
    }
    
    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    //generate token for user
    public String generateToken(LoginVO loginVO) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, loginVO.getUserSe()+loginVO.getId());
    }

    public String generateToken(LoginVO loginVO, Map<String, Object> claims) {
        return doGenerateToken(claims, loginVO.getUserSe()+loginVO.getId());
    }
    
	//while creating the token -
	//1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
	//2. Sign the JWT using the HS512 algorithm and secret key.
	//3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	//   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {
    	System.out.println("===>>> secret = "+secretKey);

        Map<String, Object> header = new HashMap<>();
        header.put("typ", typ);
        header.put("alg", alg);

        SecretKey key = null;
        try {
            key = Keys.hmacShaKeyFor(secretKey.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw BizRuntimeException.create(e.getMessage());
        }

        long now = System.currentTimeMillis();
        
        return Jwts.builder()
            .setHeader(header)
            .setIssuer(issuer)
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(now + JWT_TOKEN_VALIDITY * 1000))
            .setClaims(claims)
            .setSubject(subject)
            //.signWith(SignatureAlgorithm.HS512, key)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }
    
    //validate token
    public Boolean validateToken(String token, LoginVO loginVO) {
        final String username = getUsernameFromToken(token);
        return (username.equals(loginVO.getUserSe()+loginVO.getId()) && !isTokenExpired(token));
    }

}
