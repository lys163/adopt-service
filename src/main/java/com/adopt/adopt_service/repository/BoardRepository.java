package com.adopt.adopt_service.repository;




import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adopt.adopt_service.domain.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{
    Page<Board> findByUser_userId(Long userId,Pageable pageable);
}
