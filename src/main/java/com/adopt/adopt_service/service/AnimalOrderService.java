package com.adopt.adopt_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.adopt.adopt_service.domain.AdoptState;
import com.adopt.adopt_service.domain.Animal;
import com.adopt.adopt_service.domain.AnimalOrder;
import com.adopt.adopt_service.domain.AnimalState;
import com.adopt.adopt_service.domain.User;
import com.adopt.adopt_service.repository.AnimalRepository;
import com.adopt.adopt_service.repository.OrderAnimalRepository;
import com.adopt.adopt_service.repository.UserRepository;
import com.adopt.adopt_service.repository.animalStateRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnimalOrderService {
    
    private final OrderAnimalRepository orderAnimalRepository;
    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;
    private final animalStateRepository animalStateRepository; 

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

    @Transactional
    public void acceptOrder(Long orderId) {
        AnimalOrder targetOrder = orderAnimalRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        Animal animal = targetOrder.getAnimal();

        Long animalId = animal.getAnimalId();

        // 해당 동물에 걸린 모든 주문 조회
        List<AnimalOrder> orders = orderAnimalRepository.findByAnimalAnimalId(animalId);

        for (AnimalOrder order : orders) {
            if (order.getOrderId().equals(orderId)) {
                order.accept();
            } else {
                order.reject();
            }
        }
        orderAnimalRepository.saveAll(orders);

        // Adopt 상태 변경
        AnimalState animalState = animal.getAnimalState();
        log.info("aaa"+(animalState == null));
        if (animalState == null) {
            animalState = AnimalState.builder()
                .animal(animal) 
                .adoptState(AdoptState.ADOPTED)
                .build();
            animal.setAnimalState(animalState); 
        } else {
            animalState.setAdoptState(AdoptState.ADOPTED);
        }
        animalStateRepository.save(animalState);
        // animalState cascade가 안 되어있으면 따로 저장 필요
        animalRepository.save(animal);
    }
    

    public void deleteOrder(Long orderId){
        orderAnimalRepository.deleteById(orderId);
    }

    public List<AnimalOrder> getOrderByAnimalId(Long animalId) {
        return orderAnimalRepository.findByAnimalAnimalId(animalId);
    }
}
