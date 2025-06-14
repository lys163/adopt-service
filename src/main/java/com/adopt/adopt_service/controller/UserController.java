package com.adopt.adopt_service.controller;




import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Sort;

import com.adopt.adopt_service.dto.BoardResponse;
import com.adopt.adopt_service.dto.RegisterRequest;
import com.adopt.adopt_service.dto.UpdateUserRequest;
import com.adopt.adopt_service.dto.UserResponse;
import com.adopt.adopt_service.repository.UserRepository;
import com.adopt.adopt_service.service.BoardService;
import com.adopt.adopt_service.service.CustomUserDetails;
import com.adopt.adopt_service.service.CustomUserDetailsService;
import com.adopt.adopt_service.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    
    private final UserRepository userRepository;

    private final UserService userService;

    private final BoardService boardService;

    private final CustomUserDetailsService customUserDetailsService;
    

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
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
            .<ResponseEntity<Object>>map(user -> ResponseEntity.ok(new UserResponse(user.getUserId(),user.getEmail(), user.getName(),user.getPn(),user.getAge(),user.getAddr(), user.getRole())))
            .orElseGet(() -> ResponseEntity.status(404).body("사용자 없음"));
    }
    @DeleteMapping("/withdraw")
    public ResponseEntity<?> deleteUser(Authentication authentication){
        if (authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(401).body("로그인 필요");
        }
        String email = authentication.getName();
        CustomUserDetails ud = (CustomUserDetails)customUserDetailsService.loadUserByUsername(email);

        Long userId = ud.getUserId();

        userRepository.deleteById(userId);
        return ResponseEntity.status(200).body("삭제 성공!");
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(401).body("로그인 필요");
        }
        String email = authentication.getName();
        CustomUserDetails ud = (CustomUserDetails)customUserDetailsService.loadUserByUsername(email);
        Long userId = ud.getUserId();
        
        userService.updateUser(userId,request);

        return ResponseEntity.status(200).body("업데이트 됨");
    }
    @GetMapping("/boards")
    public ResponseEntity<?> getUserBoards(
        Authentication authentication,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        if (authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(401).body(Map.of("error", "로그인 필요"));
        }
        String email = authentication.getName();
        CustomUserDetails ud = (CustomUserDetails)customUserDetailsService.loadUserByUsername(email);
        Long userId = ud.getUserId();
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("boardId").descending());
        Page<BoardResponse> userBoards = boardService.getUserBoards(userId, pageable);
        return ResponseEntity.status(200).body(userBoards);
    }
}
