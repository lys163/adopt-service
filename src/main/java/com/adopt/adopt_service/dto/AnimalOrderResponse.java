package com.adopt.adopt_service.dto;

public record AnimalOrderResponse(
    Long orderId,
    String orderDate,
    UserResponse user,
    AnimalResponse animal
) {
    
}
