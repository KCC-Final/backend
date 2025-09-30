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
		return List.of(() -> "ROLE_USER"); // 기본 유저 권한 부여
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserId(); //로그인 기준 값
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; 
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; 
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; 
    }

    @Override
    public boolean isEnabled() {
        return user.isEmailVerified();
    }
}