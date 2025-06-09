package com.adopt.adopt_service.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.adopt.adopt_service.domain.User;
import com.adopt.adopt_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService{
    @Autowired    
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByemail(username)
        .orElseThrow(()->new UsernameNotFoundException("사용자 없음"));
        log.info(user+"aaaaaaaaaaaaabbbbbbb");
        return new CustomUserDetails(user);
        
    }
}