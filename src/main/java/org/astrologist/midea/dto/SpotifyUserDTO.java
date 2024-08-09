package org.astrologist.midea.dto;

public class SpotifyUserDTO {

    private String id;       // Spotify 사용자 ID
    private String email;    // Spotify 사용자 이메일

    // 기본 생성자
    public SpotifyUserDTO() {
    }

    // 모든 필드를 초기화하는 생성자
    public SpotifyUserDTO(String id, String email) {
        this.id = id;
        this.email = email;
    }

    // Getter 및 Setter 메서드
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}