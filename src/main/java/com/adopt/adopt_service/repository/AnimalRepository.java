package com.adopt.adopt_service.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.adopt.adopt_service.domain.Animal;


@Repository
public interface AnimalRepository extends JpaRepository<Animal,Long>{
    Page<Animal> findByUserUserId(Long userId, Pageable pageable);
}
