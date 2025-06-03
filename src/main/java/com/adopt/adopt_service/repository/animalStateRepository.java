package com.adopt.adopt_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adopt.adopt_service.domain.AnimalState;

@Repository
public interface animalStateRepository extends JpaRepository<AnimalState,Long>{

} 
