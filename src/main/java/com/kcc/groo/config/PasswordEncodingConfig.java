package com.kcc.groo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncodingConfig {
    @Bean 
    public PasswordEncoder passwordEncoder() { //인터페이스: 다양한 암호화 전략 추상화
    	//스프링 시큐리티에서 제공하는 암호화 방식 중 하나
    	//비밀번호를 해싱처리
        return new BCryptPasswordEncoder();
    }
}
