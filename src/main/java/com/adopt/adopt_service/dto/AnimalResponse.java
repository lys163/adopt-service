package com.adopt.adopt_service.dto;

import java.util.List;

import com.adopt.adopt_service.domain.AnimalState;
import com.adopt.adopt_service.domain.Size;
import com.adopt.adopt_service.domain.Type;

public record AnimalResponse(
    Long animalId,
    String name,
    String age,
    float kg,
    Size size,
    Type type,
    String description,
    List<String> personalities,
    List<String> imageBase64s,
    int view,
    int likes,
    String userName,
    String userEmail,
    AnimalState animalState,
    Long userId
) {
} 