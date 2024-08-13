package org.astrologist.midea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.astrologist.midea.entity.User;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MindlistAdminDTO {

    private Long mno;
    private String composer;
    private String title;
    private String content;
    private String url;
    private String writer;
    private LocalDateTime regDate, modDate;

    // Entity to DTO conversion
    public static UserPageDTO fromEntity(User user) {
        UserPageDTO dto = new UserPageDTO();
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

