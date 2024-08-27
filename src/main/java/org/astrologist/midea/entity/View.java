package org.astrologist.midea.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"mindlist", "mindlistAdmin"})
public class View {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vno;

    private Long aMno; //관리자 게시글 번호

    private Long uMno; //유저 게시글 번호

    @ManyToOne(fetch = FetchType.LAZY)
    private Mindlist mindlist;

    @ManyToOne(fetch = FetchType.LAZY)
    private MindlistAdmin mindlistAdmin;

}
