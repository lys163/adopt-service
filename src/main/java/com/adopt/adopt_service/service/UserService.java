package com.adopt.adopt_service.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.adopt.adopt_service.domain.User;
import com.adopt.adopt_service.dto.BoardResponse;
import com.adopt.adopt_service.dto.RegisterRequest;
import com.adopt.adopt_service.dto.UpdateUserRequest;
import com.adopt.adopt_service.repository.BoardRepository;
import com.adopt.adopt_service.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void updateUser(Long userId, UpdateUserRequest request){
        User user = userRepository.findById(userId)
            .orElseThrow(()-> new RuntimeException("해당유저 찾을 수 없음"));
        // 클라이언트에서는 수정페이지에서 pw(비밀번호) 데이터를 빈칸으로 줌
        if (request.pw() != null && !request.pw().isBlank()) {
            user.setPw(passwordEncoder.encode(request.pw()));
        }
        user.setName(request.name());
        user.setPn(request.pn());
        user.setAge(request.age());
        user.setAddr(request.addr());

        userRepository.save(user);
    }
    
    
}
