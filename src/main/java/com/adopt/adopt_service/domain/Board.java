package com.adopt.adopt_service.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="board")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")  // User 테이블의 PK와 연결됨
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "create_time")
    private LocalDateTime creatTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    public static Board create(User user, String title, String content) {
        return new Board(
            null,
            user,
            title,
            content,
            null,
            null
        );
    }

    @PrePersist
    public void onCreate() {
        this.creatTime = LocalDateTime.now();
        this.updateTime = this.creatTime;
    }


    @PreUpdate
    public void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getWriterName() {
        return user != null ? user.getName() : null;
    }
}
