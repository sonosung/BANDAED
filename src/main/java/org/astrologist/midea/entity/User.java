package org.astrologist.midea.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @Builder.Default
    @Column(nullable = false)
    private boolean emailActive = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean nicknameActive = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean happy = false;

    @Builder.Default
    @Column(nullable = false)
    private boolean sad = false;

    @Builder.Default
    @Column(nullable = false)
    private boolean calm = false;

    @Builder.Default
    @Column(nullable = false)
    private boolean stressed = false;

    @Builder.Default
    @Column(nullable = false)
    private boolean joyful = false;

    @Builder.Default
    @Column(nullable = false)
    private boolean energetic = false;

    public enum UserRole {
        GUEST, MEMBER, ADMIN
    }
}
