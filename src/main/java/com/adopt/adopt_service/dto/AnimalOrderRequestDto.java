package com.adopt.adopt_service.dto;

public record AnimalOrderRequestDto(
    Long userId,
    Long AnimalId,
    String orderDate
) {
    
}
