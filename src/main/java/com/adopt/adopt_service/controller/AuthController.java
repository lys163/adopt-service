package com.adopt.adopt_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adopt.adopt_service.domain.User;
import com.adopt.adopt_service.dto.LoginRequest;
import com.adopt.adopt_service.dto.RegisterRequest;
import com.adopt.adopt_service.repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        // 로그인 처리 로직
        try{
            Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.pw())
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);

            HttpSession session = httpRequest.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            // session.setAttribute("SPRINGA_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            // session.setMaxInactiveInterval();
            
            return ResponseEntity.ok("로그인 성공");
        }catch(BadCredentialsException e){
            log.info("로그인 시도: email={}, pw={}", request.email(), request.pw());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호 오류");
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if(userRepository.findByemail(request.email()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 이메일입니다.");
        }
        User user=new User(
        request.email(),
        passwordEncoder.encode(request.pw()),
        request.name(),
        request.pn(),
        request.age(),
        request.addr(),
        "ROLE_USER"
        );
        userRepository.save(user);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/adminregister")
    public ResponseEntity<?> adminRegister(@RequestBody RegisterRequest request) {
        if(userRepository.findByemail(request.email()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 이메일입니다.");
        }
        User user=new User(
        request.email(),
        passwordEncoder.encode(request.pw()),
        request.name(),
        request.pn(),
        request.age(),
        request.addr(),
        "ROLE_ADMIN"
        );
        userRepository.save(user);
        return ResponseEntity.ok("회원가입 성공");
    }

    // @PostMapping("/logout")
    // public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
    //     HttpSession session = request.getSession(false);
    //     if (session != null) session.invalidate();

    //     Cookie cookie = new Cookie("JSESSIONID", null);
    //     cookie.setPath("/"); 
    //     cookie.setHttpOnly(true);
    //     cookie.setMaxAge(0);
    //     response.addCookie(cookie);

    //     SecurityContextHolder.clearContext();
    //     return ResponseEntity.ok("로그아웃 성공");
    // }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok("로그아웃 성공");
    }

    @GetMapping("/check-login")
    public ResponseEntity<?> checkLogin(Authentication authentication) {
    if (authentication != null && authentication.isAuthenticated() 
        && !(authentication instanceof AnonymousAuthenticationToken)) {
        return ResponseEntity.ok("로그인됨");
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인되지 않음");
    }
    
}
