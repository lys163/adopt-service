package com.adopt.adopt_service.dto;

import java.time.LocalDateTime;

import com.adopt.adopt_service.domain.Board;

public record BoardResponse(
    Long boardId,
    String title,
    String content,
    String writer,
    String writerEmail,
    LocalDateTime create_time,
    LocalDateTime update_time
) {
    public static BoardResponse from(Board board) {
        return new BoardResponse(
            board.getBoardId(),
            board.getTitle(),
            board.getContent(),
            board.getWriterName(),
            board.getUser().getEmail(),
            board.getCreatTime(), 
            board.getUpdateTime());
    }
}
