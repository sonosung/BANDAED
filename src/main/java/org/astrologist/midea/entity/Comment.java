package org.astrologist.midea.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"mindlist", "mindlistAdmin", "commenter"})
public class Comment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    private User commenter;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mindlist mindlist;

    @ManyToOne(fetch = FetchType.LAZY)
    private MindlistAdmin mindlistAdmin;

}
