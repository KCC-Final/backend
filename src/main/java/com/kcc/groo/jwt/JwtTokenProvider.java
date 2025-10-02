package com.kcc.groo.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.kcc.groo.user.data.model.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kys
 * @created 2025-09-15
 * JWT 토큰의 생성, 파싱, 검증 담당 클래스
 */
@Slf4j
@Component
public class JwtTokenProvider {
	
	private static final String SECRET = "mF8zdJXuTPGGUpO6DYRRby62knsq3ozQ9dWbQ/2QUvI="; //고정된 값
	private static final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
//	private static final String AUTH_HEADER = "Authorization";
	
	//토큰 만료 시간
	private final long accessTokenValidTime = 15 * 60 * 1000L; //15min
	private final long refreshTokenValidTime = 7 * 24 * 60 * 60 * 1000L; //7 days
	
	@Autowired
	UserDetailsService userDetailsService;
	
	
	/**
	 * @param user
	 * @created 2025-09-29
	 * @return jwt token
	 * 사용자 정보를 기반으로 access token 생성
	 */
	public String generateAccessToken (Users user) {
		long now = System.currentTimeMillis();
		Claims claims = Jwts.claims()
				.subject(user.getUserId()) //토큰명
				.setIssuer("https://groo.site").issuedAt(new Date(now)) //토큰 발급자 도메인으로 설정
				.expiration(new Date(now + accessTokenValidTime))
				.add("name", user.getName()) //이름
				.add("nickname", user.getNickname()) //닉네임
				.add("email", user.getEmail()) //이메일
				.build();
		
		return Jwts.builder().claims(claims).signWith(key).compact();
	}
	
	/**
	 * @param user
	 * @created 2025-09-29
	 * @return jwt token
	 * 사용자 정보를 기반으로 refresh token 생성
	 */
	public String generateRefreshToken (Users user) {
		long now = System.currentTimeMillis();
		Claims claims = Jwts.claims()
				.subject(user.getUserId())
				.setIssuer(user.getNickname()).issuedAt(new Date(now))
				.expiration(new Date(now + refreshTokenValidTime))
				.build();
		
		return Jwts.builder().claims(claims).signWith(key).compact();
	}
	
	
	/**
	 * @param request
	 * @author kys
	 * @created 2025-09-15
	 * @return accessToken
	 * 
	 * @modified 2025-09-29
	 * token 값 검증 및 반환
	 */
	public String resolveAccessToken (HttpServletRequest request) {		
		
		if (request.getCookies() != null) { //cookie에서 토큰 가져옴
			for (Cookie cookie : request.getCookies()) {
				if ("accessToken".equals(cookie.getName())) { //access token이 있을 경우 값 리턴
					return cookie.getValue();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * @param request
	 * @author kys
	 * @created 2025-09-15
	 * @return refreshToken
	 * 
	 * @modified 2025-09-29
	 * token 값 검증 및 반환
	 */
	public String resolveRefreshToken (HttpServletRequest request) {		
		
		if (request.getCookies() != null) { //cookie에서 토큰 가져옴
			for (Cookie cookie : request.getCookies()) {
				if ("refreshToken".equals(cookie.getName())) { //refreshToken token이 있을 경우 값 리턴
					return cookie.getValue();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * @param token
	 * @author kys
	 * @created 2025-09-15
	 * @return Claims
	 * JWT 문자열을 Claims 객체로 파싱
	 */
	private Claims parseClaims(String token) {
		log.info(token);
		
		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
	/**
	 * @param token
	 * @author kys
	 * @created 2025-09-15
	 * @return userId
	 * 토큰에서 사용자 ID(subject)를 추출
	 */
	public String getUserId (String token) {
		return parseClaims(token).getSubject();
	}
	
	/**
	 * @param token
	 * @author kys
	 * @created 2025-09-15
	 * @return Authentication
	 * 토큰 기반으로 Authentication 객체를 생성
	 * Spring Security의 인증 객체로 변환되어 SecurityContext에 저장 가능
	 */
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(getUserId(token));
		log.info(userDetails.getUsername());
		
		return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
	}
	
	/**
	 * @param token
	 * @author kys
	 * @created 2025-09-15
	 * @return true/false
	 * JWT 유효성을 검증
	 */
	public boolean validateToken(String token) {
		try {
			Claims claims = parseClaims(token);
			return !claims.getExpiration().before(new Date());
		} catch (Exception e) {
			log.error("TOKEN VALIDATION FAILED: {}", e.getMessage());
			return false;
		}
	}

}
