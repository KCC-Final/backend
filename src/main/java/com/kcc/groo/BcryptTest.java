package com.kcc.groo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "pw_01";   // 평문 비밀번호
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }
}