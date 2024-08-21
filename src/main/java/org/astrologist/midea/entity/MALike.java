package org.astrologist.midea.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MAlikes")

public class MALike {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;  // 좋아요의 고유 ID (기본 키)

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private User user;  // 좋아요를 누른 사용자 (외래 키)

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "mno", nullable = false)
        private MindlistAdmin mno;  // 좋아요가 눌린 게시물 (외래 키)
    }

