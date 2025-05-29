package com.adopt.adopt_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adopt.adopt_service.repository.PersonalityRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/personalities")
@RequiredArgsConstructor
public class PersonalityController {

    private final PersonalityRepository personalityRepository;

    @GetMapping
    public ResponseEntity<List<String>> getAllPersonalities() {
        List<String> names = personalityRepository.findAll().stream()
            .map(p->p.getName())
            .toList();
        
        return ResponseEntity.status(HttpStatus.OK).body(names);
    }
    
    
}
