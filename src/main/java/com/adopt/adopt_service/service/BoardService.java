package com.adopt.adopt_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.adopt.adopt_service.domain.Board;
import com.adopt.adopt_service.domain.User;
import com.adopt.adopt_service.dto.BoardRequest;
import com.adopt.adopt_service.dto.BoardResponse;
import com.adopt.adopt_service.repository.BoardRepository;
import com.adopt.adopt_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public BoardResponse createBoard(BoardRequest boardRequest, String email) {
        User user = userRepository.findByemail(email)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));
        Board board = Board.create(user, boardRequest.title(), boardRequest.content());
        return BoardResponse.from(boardRepository.save(board));
    }

    public Page<BoardResponse> getBoards(Pageable pageable) {
        Page<Board> boards = boardRepository
                .findAll(pageable);
        return boards.map(BoardResponse::from);
    }

    public BoardResponse getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()->new RuntimeException("게시글 없음"));
            return BoardResponse.from(board);
    }

    public BoardResponse updateBoard(Long boardId, BoardRequest boardRequest) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()->new RuntimeException("게시글 없음"));
        board.update(boardRequest.title(), boardRequest.content());
        return BoardResponse.from(boardRepository.save(board));
    }

    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()->new RuntimeException("게시글 없음"));
        boardRepository.delete(board);
    }
    
    public Page<BoardResponse> getUserBoards(Long userId, Pageable pageable) {
        return boardRepository.findByUser_userId(userId, pageable)
                .map(BoardResponse::from);
    }
}
