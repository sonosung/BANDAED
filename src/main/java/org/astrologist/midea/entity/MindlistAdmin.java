package org.astrologist.midea.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString(exclude = "userIdx")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MindlistAdmin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Column(length = 100, nullable = false)
    private String nickname;

    @ManyToOne (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User userIdx;  // 이메일 필드, 고유값이며 필수

    @Column(length = 100, nullable = false)
    private String composer;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 1500, nullable = false)
    private String content;

    @Column(length = 1500, nullable = false)
    private String url;

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

    @Builder.Default
    @Column(nullable = false)
    private int likeCount = 0;  // 게시물에 대한 총 좋아요 수

    public void changeComposer(String composer) {
        this.composer = composer;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeUrl(String url) {
        this.url = url;
    }

    public void changeHappy(boolean happy) {
        this.happy = happy;
    }

    public void changeSad(boolean sad) {
        this.sad = sad;
    }

    public void changeCalm(boolean calm) {
        this.calm = calm;
    }

    public void changeStressed(boolean stressed) {
        this.stressed = stressed;
    }

    public void changeJoyful(boolean joyful) {
        this.joyful = joyful;
    }

    public void changeEnergetic(boolean energetic) {
        this.energetic = energetic;
    }

}

