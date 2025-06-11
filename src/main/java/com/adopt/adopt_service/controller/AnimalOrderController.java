package com.adopt.adopt_service.controller;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adopt.adopt_service.domain.AnimalOrder;
import com.adopt.adopt_service.domain.User;
import com.adopt.adopt_service.dto.AnimalOrderResponse;
import com.adopt.adopt_service.dto.AnimalOrderUserResponse;
import com.adopt.adopt_service.mapper.AnimalOrderMapper;
import com.adopt.adopt_service.repository.UserRepository;
import com.adopt.adopt_service.service.AnimalOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class AnimalOrderController {
    
    private final AnimalOrderService animalOrderService;
    private final UserRepository userRepository;
    private final AnimalOrderMapper animalOrderMapper;

    // 생성
    @PostMapping("/{animalId}")
    public ResponseEntity<?> createOrder(@PathVariable Long animalId, Authentication authentication){
        if (authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 안되어있음");
        }
        String email = authentication.getName();

        User user = userRepository.findByemail(email)
            .orElseThrow(()->new IllegalArgumentException("사용자없음"));

        AnimalOrder order = animalOrderService.createOrder(user.getUserId(), animalId);
        AnimalOrderResponse response = animalOrderMapper.toDto(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/uploader/{animalId}")
    public ResponseEntity<?> getMethodName(@PathVariable Long animalId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 안되어있음");
        }
        String email = authentication.getName();

        User user = userRepository.findByemail(email)
            .orElseThrow(()->new IllegalArgumentException("사용자없음"));
        List<AnimalOrder> order = animalOrderService.getOrderByAnimal(animalId);

        List<AnimalOrderResponse> responses = order.stream()
            .map(AnimalOrderMapper::toDto)
            .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
    
    // 현재 로그인 유저의 모든 주문 조회
    @GetMapping("/my")
    public ResponseEntity<?> getMyOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
        ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        String email = authentication.getName();
        User user = userRepository.findByemail(email)
            .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        Page<AnimalOrder> orders = animalOrderService.getOrderByUser(user.getUserId(),PageRequest.of(page, size));

        Page<AnimalOrderResponse> dtos = orders.map(AnimalOrderMapper::toDto);

        return ResponseEntity.ok(dtos);
    }

    // 올린 동물들의 주문 모두 조회
    @GetMapping("/uploader")
    public ResponseEntity<?> getOrdersForMyAnimals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
        ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        String email = authentication.getName();
        User currentUser = userRepository.findByemail(email)
            .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        Page<AnimalOrder> orders = animalOrderService.getOrdersByUploader(currentUser.getUserId(),PageRequest.of(page, size));
        Page<AnimalOrderResponse> responses = orders.map(AnimalOrderMapper::toDto);

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        String email = authentication.getName();
        User user = userRepository.findByemail(email)
            .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        return animalOrderService.getOrderById(orderId)
            .map(order -> {
                if (!order.getUser().getUserId().equals(user.getUserId())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("본인의 주문만 삭제할 수 있습니다.");
                }
                animalOrderService.deleteOrder(orderId);
                return ResponseEntity.noContent().build();
            })
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문을 찾을 수 없습니다."));
    }

    @PatchMapping("/{orderId}/accept")
    public ResponseEntity<Void> acceptOrder(
            @PathVariable Long orderId
    ) {
        animalOrderService.acceptOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{animalId}")
    public ResponseEntity<?> getOrderByAnimal(@PathVariable Long animalId, Authentication authentication){
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        
        String email = authentication.getName();
        User user = userRepository.findByemail(email)
            .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        List<AnimalOrder> orders = animalOrderService.getOrderByAnimal(animalId);
        List<AnimalOrderUserResponse> responses = orders.stream()
            .map(order -> new AnimalOrderUserResponse(order.getUser().getUserId()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}
