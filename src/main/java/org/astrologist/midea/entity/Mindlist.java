package org.astrologist.midea.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Mindlist extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Column(length = 100, nullable = false)
    private String composer;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 1500, nullable = false)
    private String content;

    @Column(length = 1500, nullable = false)
    private String url;

    @Column(length = 50, nullable = false)
    private String nickname;

    @Column(length = 1000, nullable = false)
    private String profileImagePath;  // 사용자 프로필 이미지 경로

    public void changeComposer(String composer){
        this.composer = composer;
    }

    public void changeContent(String content){this.content = content; }

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeUrl(String url){
        this.url = url;
    }

}
