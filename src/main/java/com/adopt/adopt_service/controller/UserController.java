package com.adopt.adopt_service.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adopt.adopt_service.dto.UserResponse;
import com.adopt.adopt_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("로그인 필요");
        }

    // 인증된 사용자 정보 가져오기
        String email = authentication.getName();
    
    // Optional로 반환하여, 사용자가 존재하는지 확인
        return userRepository.findByemail(email)
            .<ResponseEntity<Object>>map(user -> ResponseEntity.ok(new UserResponse(user.getEmail(), user.getName(),user.getPn(),user.getAge(),user.getAddr())))
            .orElseGet(() -> ResponseEntity.status(404).body("사용자 없음"));
    }

}
