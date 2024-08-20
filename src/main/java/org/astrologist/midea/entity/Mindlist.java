package org.astrologist.midea.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@ToString(exclude = {"email", "nickname"})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Mindlist extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @ManyToOne (fetch = FetchType.LAZY)
    private User email;  // 이메일 필드, 고유값이며 필수

    @Column(length = 100, nullable = false)
    private String nickname;

    @Column(length = 100, nullable = false)
    private String composer;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 1500, nullable = false)
    private String content;

    @Column(length = 1500, nullable = false)
    private String url;

    public void changeComposer(String composer){
        this.composer = composer;
    }

    public void changeContent(String content){
        this.content = content; }

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeUrl(String url){
        this.url = url;
    }

}
