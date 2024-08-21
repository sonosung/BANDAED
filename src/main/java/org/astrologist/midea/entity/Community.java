package org.astrologist.midea.entity;

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