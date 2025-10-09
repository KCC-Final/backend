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
	      
	      try {
	         String token = jwtTokenProvider.resolveAccessToken(request);
	         if (token != null && jwtTokenProvider.validateToken(token)) {
	            Authentication authentication = jwtTokenProvider.getAuthentication(token);
	            SecurityContextHolder.getContext().setAuthentication(authentication);
	         }
	      } catch (Exception e) {
	         log.warn("JWT validation failed: {}", e.getMessage());
	      }
	      
	      filterChain.doFilter(request, response);
	   }
	
	
}
