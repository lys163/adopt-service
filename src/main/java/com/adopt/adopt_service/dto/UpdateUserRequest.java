package com.adopt.adopt_service.dto;

public record UpdateUserRequest(
    String pw,
    String name,
    String pn,
    int age,
    String addr
) {
} 
