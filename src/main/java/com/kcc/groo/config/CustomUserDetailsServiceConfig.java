package com.kcc.groo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.kcc.groo.user.dao.IUsersRepository;
import com.kcc.groo.user.data.model.Users;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CustomUserDetailsServiceConfig {

    private final IUsersRepository usersRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Users user = usersRepository.selectUserByUserId(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found with id: " + username);
            }
            return new CustomUserDetails(user);
        };
    }
}

