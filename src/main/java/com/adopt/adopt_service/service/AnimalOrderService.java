package com.adopt.adopt_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.adopt.adopt_service.domain.Animal;
import com.adopt.adopt_service.domain.AnimalOrder;
import com.adopt.adopt_service.domain.User;
import com.adopt.adopt_service.repository.AnimalRepository;
import com.adopt.adopt_service.repository.OrderAnimalRepository;
import com.adopt.adopt_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimalOrderService {
    
    private final OrderAnimalRepository orderAnimalRepository;
    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;

    public AnimalOrder createOrder(Long userId, Long animalId){
        User user = userRepository.findById(userId)
            .orElseThrow(()-> new IllegalArgumentException("user not found"));
        Animal animal = animalRepository.findById(animalId)
            .orElseThrow(()-> new IllegalArgumentException("user not found"));

        AnimalOrder order = AnimalOrder.builder()
            .user(user)
            .animal(animal)
            .orderDate(LocalDateTime.now().toString())
            .build();

        return orderAnimalRepository.save(order);
    }

    public Optional<AnimalOrder> getOrderById(Long orderId){
        return orderAnimalRepository.findById(orderId);
    }

    public List<AnimalOrder> getAllOrders(){
        return orderAnimalRepository.findAll();
    }

    public Page<AnimalOrder> getOrderByUser(Long userId, Pageable pageable){
        return orderAnimalRepository.findByUserUserId(userId, pageable);
    }

    public List<AnimalOrder> getOrderByAnimal(Long animalId){
        return orderAnimalRepository.findByAnimalAnimalId(animalId);
    }

    public Page<AnimalOrder> getOrdersByUploader(Long uploaderUserId,Pageable pageable) {
    return orderAnimalRepository.findByAnimalUserUserId(uploaderUserId,pageable);
}

    public void deleteOrder(Long orderId){
        orderAnimalRepository.deleteById(orderId);
    }
}
