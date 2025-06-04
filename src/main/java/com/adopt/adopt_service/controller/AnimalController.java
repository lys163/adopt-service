package com.adopt.adopt_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adopt.adopt_service.dto.AnimalRegisterRequest;
import com.adopt.adopt_service.dto.AnimalResponse;
import com.adopt.adopt_service.service.AnimalService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/animals")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AnimalRegisterRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }
        String email = authentication.getName();
        animalService.registerAnimal(request, email);
        return ResponseEntity.status(HttpStatus.CREATED).body("생성 성공");
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponse> getAnimalDetail(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(animalService.getAnimalDetail(id));
    }

    @GetMapping
    public ResponseEntity<Page<AnimalResponse>> getAnimals(
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("animalId").descending());
        return ResponseEntity.status(HttpStatus.OK).body(animalService.getAnimals(pageable));
    }
    
    @GetMapping("/user/{userId}")
        public ResponseEntity<Page<AnimalResponse>> getAnimalsByUserId(@PathVariable Long userId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("animalId").descending());
    return ResponseEntity.ok(animalService.getAnimalsByUserId(userId, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long id){
        animalService.deleteAnimal(id);
        return ResponseEntity.noContent().build();
    }
    
}
