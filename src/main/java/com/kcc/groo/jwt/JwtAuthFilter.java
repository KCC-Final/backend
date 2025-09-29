package com.kcc.groo.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//로그인 경로 예외
	    String path = request.getServletPath();
	    if (path.startsWith("/api/v1/auth") || path.startsWith("/api/v1/users/signup")) {
	        filterChain.doFilter(request, response);
	        return;
	    }
		
		try {
			String token = jwtTokenProvider.resolveToken(request);
			if (token != null && jwtTokenProvider.validateToken(token)) {
				Authentication authentication = jwtTokenProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		} catch (Exception e) {
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("{\"error\":\"Unauthorized\"}");
			return;
		}
		filterChain.doFilter(request, response);
	}
	
	
}
