package org.astrologist.midea.entity;
//커뮤니티 엔티티
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String composer;

    private String content;

    @Enumerated(EnumType.STRING)
    private Subcategory subcategory;

    private LocalDateTime timestamp;

    @Builder.Default
    @Column(nullable = false)
    private int likeCount = 0;  // 게시물에 대한 총 좋아요 수

    public enum Subcategory {
        MinimalTechno,
        MinimalHouse,
        MinimalClassical,
        SteveReich,
        PhilipGlass,
        TerryRiley,
        Synthesizer,
        Piano,
        Percussion
    }
}