package org.astrologist.midea.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "mindlist")
public class Comment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;

    private String text;

    private String commenter;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mindlist mindlist;

}
