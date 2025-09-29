package com.kcc.groo.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.kcc.groo.user.data.model.Users;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails  {
	
	private final Users user;
	
	public Users getUser() {
	    return this.user;
	}


	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(() -> "ROLE_USER"); // 기본 유저 권한 부여// 또는 권한 구현 필요 시 변경
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserId(); // 또는 이메일 등 로그인 기준 값
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 필요에 따라 로직 구현
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 필요에 따라 로직 구현
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 필요에 따라 로직 구현
    }

    @Override
    public boolean isEnabled() {
        return user.isEmailVerified(); // 예시로 이메일 인증 여부 사용
    }
}