package org.astrologist.midea.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"mindlist", "mindlistAdmin", "userIdx"})
public class View extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vno;

    @ManyToOne(fetch = FetchType.LAZY)
    private User userIdx;

    @Column(length = 100)
    private String viewUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mindlist mindlist;

    @ManyToOne(fetch = FetchType.LAZY)
    private MindlistAdmin mindlistAdmin;

}
