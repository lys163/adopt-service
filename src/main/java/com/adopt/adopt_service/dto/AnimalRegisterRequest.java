package com.adopt.adopt_service.dto;

import java.util.List;

import com.adopt.adopt_service.domain.Size;
import com.adopt.adopt_service.domain.Type;

public record AnimalRegisterRequest(
    String name,
    String age,
    float kg,
    Size size,
    Type type,
    String description,
    List<byte[]> imageBlobs,
    List<String> personalityNames
) {
    
}
