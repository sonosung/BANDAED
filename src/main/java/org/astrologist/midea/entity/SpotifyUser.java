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
@Table(name = "spotify_user")
public class SpotifyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)  // 'user_id'를 외래 키로 사용하여 User 엔티티와 연결
    private User user;

    @Column(unique = true)
    private String spotifyId;  // Spotify 사용자 ID, 고유값

    @Column
    private String spotifyAccessToken;  // Spotify 액세스 토큰

    @Column
    private String spotifyRefreshToken;  // Spotify 리프레시 토큰

    @Column
    private Long spotifyTokenExpiry;  // Spotify 토큰 만료 시간 (Unix Timestamp)

    @Builder.Default
    @Column(nullable = false)
    private boolean spotifyLoggedIn = false;  // 사용자가 Spotify에 로그인했는지 여부, 기본값은 false
}
