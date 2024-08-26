package org.astrologist.midea.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 사용자의 고유 ID (기본 키)

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    private String email;  // 이메일 필드, 고유값이며 필수

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Column(nullable = false)
    private String password;  // 패스워드 필드, 최소 6자리 이상, 필수

    @NotBlank(message = "Nickname is mandatory")
    @Column(nullable = false, unique = true)
    private String nickname;  // 닉네임 필드, 고유값이며 필수

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserRole userRole = UserRole.MEMBER;  // 사용자의 역할, 기본값은 MEMBER

    @Builder.Default
    @Column(nullable = false)
    private boolean emailActive = true;  // 이메일이 활성화되었는지 여부, 기본값은 true

    @Builder.Default
    @Column(nullable = false)
    private boolean nicknameActive = true;  // 닉네임이 활성화되었는지 여부, 기본값은 true

    @Builder.Default
    @Column(nullable = false)
    private boolean happy = false;  // 사용자 감정 필드 (예: 행복), 기본값은 false

    @Builder.Default
    @Column(nullable = false)
    private boolean sad = false;  // 사용자 감정 필드 (예: 슬픔), 기본값은 false

    @Builder.Default
    @Column(nullable = false)
    private boolean calm = false;  // 사용자 감정 필드 (예: 평온), 기본값은 false

    @Builder.Default
    @Column(nullable = false)
    private boolean stressed = false;  // 사용자 감정 필드 (예: 스트레스), 기본값은 false

    @Builder.Default
    @Column(nullable = false)
    private boolean joyful = false;  // 사용자 감정 필드 (예: 즐거움), 기본값은 false

    @Builder.Default
    @Column(nullable = false)
    private boolean energetic = false;  // 사용자 감정 필드 (예: 활기참), 기본값은 false

    @Column
    private String profileImagePath;  // 사용자 프로필 이미지 경로

    // 사용자 역할 정의 (게스트, 멤버, 관리자)
    public enum UserRole {
        GUEST, MEMBER, ADMIN
    }

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private SpotifyUser spotifyUser;  // Spotify 사용자 정보와 연관
}