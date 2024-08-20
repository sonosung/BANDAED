package org.astrologist.midea.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_posts")  // 테이블 이름은 일반적으로 복수형을 사용합니다.
public class UserPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 게시물의 고유 ID (기본 키)

    @NotBlank(message = "Title is mandatory")
    @Column(nullable = false)
    private String title;  // 게시물 제목

    @Lob
    @Column(nullable = false)
    private String content;  // 게시물 내용

    @Column(nullable = false)
    private LocalDateTime createdAt;  // 게시물 작성 시간

    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 게시물 수정 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 게시물을 작성한 사용자 (외래 키)

    @Builder.Default
    @Column(nullable = false)
    private int likeCount = 0;  // 게시물에 대한 총 좋아요 수
}
