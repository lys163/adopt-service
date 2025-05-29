package com.adopt.adopt_service.controller;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.adopt.adopt_service.dto.UserResponse;
import com.adopt.adopt_service.repository.UserRepository;
import com.adopt.adopt_service.service.CustomUserDetails;
import com.adopt.adopt_service.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("로그인 필요");
        }

    // 인증된 사용자 정보 가져오기
        String email = authentication.getName();
        log.info("인증된 사용자 details: {}", authentication.getDetails());
        log.info("인증된 사용자 getPrincipal: {}", authentication.getPrincipal());
        log.info("인증된 사용자 getName(): {}", authentication.getName());
        log.info("인증된 사용자 getAuthorities(): {}", authentication.getAuthorities());

        CustomUserDetails ud= (CustomUserDetails)customUserDetailsService.loadUserByUsername(email);
        log.info("인증된 사용자: {}", ud.getUsername());
        
            
    // Optional로 반환하여, 사용자가 존재하는지 확인
        return userRepository.findByemail(email)
            .<ResponseEntity<Object>>map(user -> ResponseEntity.ok(new UserResponse(user.getEmail(), user.getName(),user.getPn(),user.getAge(),user.getAddr(), user.getRole())))
            .orElseGet(() -> ResponseEntity.status(404).body("사용자 없음"));
    }

}
