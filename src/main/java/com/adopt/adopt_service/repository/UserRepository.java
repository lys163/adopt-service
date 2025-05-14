package com.adopt.adopt_service.repository;




import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adopt.adopt_service.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findByemail(String email);
    
}
