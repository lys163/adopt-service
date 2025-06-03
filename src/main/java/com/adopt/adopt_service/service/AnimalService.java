package com.adopt.adopt_service.service;



import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.adopt.adopt_service.domain.AdoptState;
import com.adopt.adopt_service.domain.Animal;
import com.adopt.adopt_service.domain.AnimalDetail;
import com.adopt.adopt_service.domain.AnimalImage;
import com.adopt.adopt_service.domain.AnimalState;
import com.adopt.adopt_service.domain.Personality;
import com.adopt.adopt_service.domain.User;
import com.adopt.adopt_service.dto.AnimalRegisterRequest;
import com.adopt.adopt_service.dto.AnimalResponse;
import com.adopt.adopt_service.repository.AnimalRepository;
import com.adopt.adopt_service.repository.UserRepository;

import jakarta.transaction.Transactional;

import com.adopt.adopt_service.repository.PersonalityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimalService {
    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;
    private final PersonalityRepository PersonalityRepository;

    public void registerAnimal(AnimalRegisterRequest request, String userEmail){
        User user=userRepository.findByemail(userEmail)
            .orElseThrow(()-> new IllegalArgumentException("사용자 없음"));
        
        AnimalDetail detail = AnimalDetail.builder()
            .description(request.description())
            .build();
        
        List<AnimalImage> images = request.imageBlobs().stream()
            .map(blob -> AnimalImage.builder()
                .imageUrl(blob)
                .build())
            .toList();
        AnimalState animalState = AnimalState.builder()
            .adoptState(AdoptState.NOT_ADOPTED)
            .build();
        
        Set<Personality> personalities = request.personalityNames().stream()
            .map(name -> PersonalityRepository.findByName(name)
                .orElseGet(()-> PersonalityRepository.save(
                    Personality.builder()
                        .name(name)
                        .build())))
            .collect(Collectors.toSet());

        Animal animal = Animal.builder()
            .user(user)
            .name(request.name())
            .age(request.age())
            .kg(request.kg())
            .size(request.size())
            .type(request.type())
            .view(0)
            .likes(0)
            .createTime(LocalDateTime.now().toString())
            .animalDetail(detail)
            .images(images)
            .personalities(personalities)
            .animalState(animalState)
            .build();

        animalState.setAnimal(animal);
        detail.setAnimal(animal);
        animal.setAnimalDetail(detail);
        images.forEach(img -> img.setAnimal(animal));
        personalities.forEach(p->p.getAnimals().add(animal));

        animalRepository.save(animal);
    }

    public AnimalResponse getAnimalDetail(Long animalId){
        Animal animal = animalRepository.findById(animalId)
            .orElseThrow(()-> new IllegalArgumentException("존재하지 않음"));
        List<String> imageBase64s = animal.getImages().stream()
            .map(img -> Base64.getEncoder().encodeToString(img.getImageUrl()))
            .toList();
        
        List<String> personalities = animal.getPersonalities().stream()
            .map(Personality::getName)
            .toList();
        
        return new AnimalResponse(
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
    }

    public Page<AnimalResponse> getAnimals(Pageable pageable){
        return animalRepository.findAll(pageable)
            .map(animal -> {
                List<String> imageBase64s = animal.getImages().stream()
                    .map(img -> Base64.getEncoder().encodeToString(img.getImageUrl()))
                    .toList();
                List<String> personalities = animal.getPersonalities().stream()
                    .map(Personality::getName)
                    .toList();
                return new AnimalResponse(
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
            });
    }
    public Page<AnimalResponse> getAnimalsByUserId(Long userId, Pageable pageable){
        return animalRepository.findByUserUserId(userId, pageable)
            .map(animal -> {
                List<String> imageBase64s = animal.getImages().stream()
                    .map(img -> Base64.getEncoder().encodeToString(img.getImageUrl()))
                    .toList();
        
                List<String> personalities = animal.getPersonalities().stream()
                    .map(Personality::getName)
                    .toList();
                return new AnimalResponse(
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
            });
    }

    @Transactional
    public void deleteAnimal(Long animalId){
        Animal animal = animalRepository.findById(animalId)
            .orElseThrow(()-> new IllegalArgumentException("동물 찾을수 없음"));
        animalRepository.delete(animal);
    }
}
