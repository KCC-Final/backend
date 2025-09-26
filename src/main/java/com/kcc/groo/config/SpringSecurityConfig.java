package com.kcc.groo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kcc.groo.jwt.JwtAuthFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
	
	@Autowired
	JwtAuthFilter jwtAuthFilter;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    
	    http.csrf((csrf)->csrf.disable());
	    
	    http.authorizeHttpRequests((authHttpReq) -> authHttpReq
	            .requestMatchers(
	                "/swagger-ui.html",
	                "/swagger-ui/**",
	                "/v3/api-docs/**",
	                "/api-docs/**",
	                "/actuator/**",
	                "/api-docs/swagger-config",
	                "/api/test/ping"
	            ).permitAll()
	            
	            // 로그인/회원가입 / 아이디 / 비밀번호 찾기 인증 허용
	            .requestMatchers(
	                "/api/v1/auth/login",
	                "/api/v1/users/signup",
	                "/api/v1/users/id/**",
	                "/api/v1/users/password",
	                "/api/v1/email",
	                "/api/v1/email/verify"
	            ).permitAll()
	            
	            // 나머지는 인증 필요
	            .anyRequest().authenticated()
	    );

	    
	    http.sessionManagement((session)-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	    http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
	    
	    return http.build();
	}

}
