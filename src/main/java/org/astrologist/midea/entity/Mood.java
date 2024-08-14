package org.astrologist.midea.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Builder.Default
    @Column(nullable = false)
    private boolean happy = false;  // 사용자 감정 필드 (예: 행복), 기본값은 false

    @Builder.Default
    @Column(nullable = false)
    private boolean sad = false;  // 사용자 감정 필드 (예: 슬픔), 기본값은 false

    @Builder.Default
    @Column(nullable = false)
    private boolean calm = false;  // 사용자 감정 필드 (예: 평온), 기본값은 false

    @Builder.Default
    @Column(nullable = false)
    private boolean stressed = false;  // 사용자 감정 필드 (예: 스트레스), 기본값은 false

    @Builder.Default
    @Column(nullable = false)
    private boolean joyful = false;  // 사용자 감정 필드 (예: 즐거움), 기본값은 false

    @Builder.Default
    @Column(nullable = false)
    private boolean energetic = false;  // 사용자 감정 필드 (예: 활기참), 기본값은 false
}
