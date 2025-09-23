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

        http.csrf(csrf -> csrf.disable());

        // ✅ WebConfig 의 전역 CORS 설정과 연결
        http.cors(cors -> {});

        http.authorizeHttpRequests(auth -> auth
            // Swagger & API Docs
            .requestMatchers(
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/api-docs/**",
                "/api-docs/swagger-config",
                "/api/health",
                "/api/v1/health",
                "/actuator/**"
            ).permitAll()

            // Auth (로그인/회원가입)
            .requestMatchers(
                "/api/v1/auth/login",
                "/api/v1/users/signup"
            ).permitAll()

            .requestMatchers(
                "/api/health",
                "/api/v1/health"
            ).permitAll()

            // 나머지는 인증 필요
            .anyRequest().authenticated()
        );

        http.sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
