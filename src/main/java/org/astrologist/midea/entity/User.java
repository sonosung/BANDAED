package org.astrologist.midea.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Nickname is mandatory")
    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserRole userRole = UserRole.MEMBER;

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
