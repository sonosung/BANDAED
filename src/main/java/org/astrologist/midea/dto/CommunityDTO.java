package org.astrologist.midea.dto;

import lombok.*;
import org.astrologist.midea.entity.Community;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDTO {
    private Long userId;
    private String composer;
    private String content;
    private String subcategory;  // Community 엔티티의 Subcategory 열거형을 사용

    @Override
    public String toString() {
        return "CommunityDTO{" +
                "userId=" + userId +
                ", composer='" + composer + '\'' +
                ", content='" + content + '\'' +
                ", subcategory='" + subcategory + '\'' +
                '}';
    }
}