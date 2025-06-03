package com.adopt.adopt_service.dto;

public record UserResponse(
    Long userId,
    String email,
    String name,
    String pn,
    int age,
    String addr,
    String role
    ) {
    
    
}
