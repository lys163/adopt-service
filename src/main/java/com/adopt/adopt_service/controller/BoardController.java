package com.adopt.adopt_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adopt.adopt_service.dto.BoardRequest;
import com.adopt.adopt_service.dto.BoardResponse;
import com.adopt.adopt_service.service.BoardService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/board")
@Slf4j


public class BoardController {

    @Autowired
    private BoardService boardService;
    
    @PostMapping
    public ResponseEntity<BoardResponse> create(@RequestBody BoardRequest boardRequest, Authentication authentication){
        String email = authentication.getName();
        log.info("인증된 사용자: {}", email);
        BoardResponse boardResponse = boardService.createBoard(boardRequest, email);
        return ResponseEntity.ok(boardResponse);
    }

    @GetMapping
    public Page<BoardResponse> listPage(@RequestParam(defaultValue="0") int page,
                                        @RequestParam(defaultValue="10") int size){     
        Pageable pageable = PageRequest.of(page,size, Sort.by("boardId").descending()); 
        return boardService.getBoards(pageable);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponse> detail(@PathVariable Long boardId){
        return ResponseEntity.ok(boardService.getBoard(boardId));
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<BoardResponse> update(@PathVariable String boardId, @RequestBody BoardRequest boardRequest) {
        return ResponseEntity.ok(boardService.updateBoard(Long.parseLong(boardId), boardRequest));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> delete(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok("게시글 삭제 완료");
    }
}
