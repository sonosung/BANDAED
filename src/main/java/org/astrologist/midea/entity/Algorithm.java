package org.astrologist.midea.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"user", "mindlist", "mindlistAdmin"})
public class Algorithm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ano;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mindlist mindlist;

    @ManyToOne(fetch = FetchType.LAZY)
    private MindlistAdmin mindlistAdmin;

    private Long likeCount;

    private Long viewCount;

    private Long matchingPercent;

}

