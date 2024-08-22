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
@Table(name = "midea_like")
public class MideaLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 고유 ID (기본 키)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 좋아요를 누른 사용자 (외래 키)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Community", nullable = true)
    private Community post1;  // Community의 좋아요 (외래 키)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Mindlist", nullable = true)
    private Mindlist post2;  // Mindlist의 좋아요 (외래 키)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MindlistAdmin", nullable = true)
    private MindlistAdmin post3;  // MindlistAdmin의 좋아요 (외래 키)
}