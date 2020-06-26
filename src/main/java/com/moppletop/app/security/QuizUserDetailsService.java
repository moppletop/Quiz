package com.moppletop.app.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Slf4j
public class QuizUserDetailsService implements UserDetailsService {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[\\d\\w]{2,10}$");

    private final PasswordEncoder passwordEncoder;
    private final String password;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        if (!USERNAME_PATTERN.matcher(name).find()) {
            throw new UsernameNotFoundException("Usernames only contain alphanumeric characters and be between 2 and 10 characters in length");
        }

        return new User(name, passwordEncoder.encode(password), Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
    }

}
