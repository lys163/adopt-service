package com.adopt.adopt_service.dto;

public record RegisterRequest(
    String email,
    String pw,
    String name,
    String pn,
    int age,
    String addr
){}
