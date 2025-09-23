package com.kcc.groo.jwt;

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
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kys
 * @since 2025-09-15
 * JWT 토큰의 생성, 파싱, 검증 담당 클래스
 */
@Slf4j
@Component
public class JwtTokenProvider {
	
	private static final SecretKey key = Jwts.SIG.HS256.key().build();
	
	private static final String AUTH_HEADER = "Authorization";
	private long tokenValidTime = 24 * 60 * 60 * 1000L; //하루

	
	@Autowired
	UserDetailsService userDetailsService;
	
	/**
	 * @param user
	 * @author kys
	 * @since 2025-09-15
	 * @return jwt token
	 * 사용자 정보를 기반으로 jwt token 생성
	 */
	public String generateToken(Users user) {
		long now = System.currentTimeMillis();
		Claims claims = Jwts.claims().subject(user.getUserId()).setIssuer(user.getNickName()).issuedAt(new Date(now))
				.expiration(new Date(now + tokenValidTime)).build();
		
		return Jwts.builder().claims(claims).signWith(key).compact();
	}
	
	/**
	 * @param request
	 * @author kys
	 * @since 2025-09-15
	 * @return AUTH_HEADER에 담긴 jwt token
	 * http 요청 헤더에서 jwt 토큰 추출
	 */
	public String resolveToken (HttpServletRequest request) {
		return request.getHeader(AUTH_HEADER);
	}
	
	/**
	 * @param token
	 * @author kys
	 * @since 2025-09-15
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
	 * @since 2025-09-15
	 * @return userId
	 * 토큰에서 사용자 ID(subject)를 추출
	 */
	public String getUserId (String token) {
		return parseClaims(token).getSubject();
	}
	
	/**
	 * @param token
	 * @author kys
	 * @since 2025-09-15
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
	 * @since 2025-09-15
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
