package com.adopt.adopt_service.dto;

public record UserResponse(
    String email,
    String name,
    String pn,
    int age,
    String addr
    ) {
    
    
}
