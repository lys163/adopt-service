package com.adopt.adopt_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adopt.adopt_service.domain.Personality;

@Repository
public interface PersonalityRepository extends JpaRepository<Personality,Long>{
    Optional<Personality> findByName(String name);
    
}
