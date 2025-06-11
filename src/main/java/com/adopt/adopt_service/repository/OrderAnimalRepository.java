package com.adopt.adopt_service.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adopt.adopt_service.domain.AnimalOrder;

@Repository
public interface OrderAnimalRepository extends JpaRepository<AnimalOrder,Long>{
    Page<AnimalOrder> findByUserUserId(Long userId,Pageable pageable);
    List<AnimalOrder> findByAnimalAnimalId(Long animalId);
    Page<AnimalOrder> findByAnimalUserUserId(Long userId, Pageable pageable);
    
} 
