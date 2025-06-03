package com.adopt.adopt_service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.adopt.adopt_service.domain.Animal;
import com.adopt.adopt_service.domain.AnimalImage;
import com.adopt.adopt_service.domain.AnimalOrder;
import com.adopt.adopt_service.domain.Personality;
import com.adopt.adopt_service.domain.User;
import com.adopt.adopt_service.dto.AnimalOrderResponse;
import com.adopt.adopt_service.dto.AnimalResponse;
import com.adopt.adopt_service.dto.UserResponse;

@Service
public class AnimalOrderMapper {
    public static AnimalOrderResponse toDto(AnimalOrder order){

        User user = order.getUser();
        Animal animal = order.getAnimal();

        UserResponse userDto = new UserResponse(
            user.getUserId(),
            user.getEmail(),
            user.getName(),
            user.getPn(),
            user.getAge(),
            user.getAddr(),
            user.getRole()
        );
        
        List<String> imageBase64s = animal.getImages().stream()
            .map(AnimalImage::getBase64)
            .collect(Collectors.toList());
        
        List<String> personalities = animal.getPersonalities().stream()
            .map(Personality::getName)
            .toList();

        AnimalResponse animalDto = new AnimalResponse(
            animal.getAnimalId(),
            animal.getName(),
            animal.getAge(),
            animal.getKg(),
            animal.getSize(),
            animal.getType(),
            animal.getAnimalDetail().getDescription(),
            personalities,
            imageBase64s,
            animal.getView(),
            animal.getLikes(),
            animal.getUser().getName(),
            animal.getUser().getEmail(),
            animal.getAnimalState(),
            animal.getUser().getUserId()
        );
        return new AnimalOrderResponse(
            order.getOrderId(),
            order.getOrderDate(),
            userDto,
            animalDto
        );
    }
}
