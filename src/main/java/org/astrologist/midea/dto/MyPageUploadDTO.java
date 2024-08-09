package org.astrologist.midea.dto;

import org.astrologist.midea.entity.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MyPageUploadDTO {

    @NotEmpty(message = "Nickname is mandatory")
    private String nickname;

    @NotEmpty(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    private boolean happy;
    private boolean sad;
    private boolean calm;
    private boolean stressed;
    private boolean joyful;
    private boolean energetic;

    private String profileImagePath;

    // DTO to Entity conversion
    public User toEntity() {
        return User.builder()
                .nickname(this.nickname)
                .email(this.email)
                .happy(this.happy)
                .sad(this.sad)
                .calm(this.calm)
                .stressed(this.stressed)
                .joyful(this.joyful)
                .energetic(this.energetic)
                .profileImagePath(this.profileImagePath)
                .build();
    }

    // Entity to DTO conversion
    public static MyPageUploadDTO fromEntity(User user) {
        MyPageUploadDTO dto = new MyPageUploadDTO();
        dto.setNickname(user.getNickname());
        dto.setEmail(user.getEmail());
        dto.setHappy(user.isHappy());
        dto.setSad(user.isSad());
        dto.setCalm(user.isCalm());
        dto.setStressed(user.isStressed());
        dto.setJoyful(user.isJoyful());
        dto.setEnergetic(user.isEnergetic());
        dto.setProfileImagePath(user.getProfileImagePath());
        return dto;
    }
}