package kr.xit.core.support.jwt;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import egovframework.com.cmm.LoginVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import kr.xit.core.exception.BizRuntimeException;
import lombok.Setter;

//security 관련 제외한 jwt util 클래스

//@Component
//@ConfigurationProperties(prefix = "app.token")
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
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (UnsupportedEncodingException e) {
            throw BizRuntimeException.create(e.getMessage());
        }
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(LoginVO loginVO) {
       // Map<String, Object> claims = new HashMap<>();
        Claims claims = Jwts.claims().setId(loginVO.getId());
        claims.put("usrSe", loginVO.getUserSe());
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

        return Jwts.builder()
            //.setHeader(header)
            //.setIssuer(issuer)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
            .setClaims(claims)
            //.setSubject(subject)
            // .signWith(SignatureAlgorithm.HS512, key)
            .signWith(SignatureAlgorithm.HS256, key)
            .compact();
    }

    //validate token
    public Boolean validateToken(String token, LoginVO loginVO) {
        final String username = getUsernameFromToken(token);
        return (username.equals(loginVO.getUserSe()+loginVO.getId()) && !isTokenExpired(token));
    }

}
