package org.astrologist.midea.dto;

import lombok.*;
import org.astrologist.midea.entity.Community;

import java.time.format.DateTimeFormatter;

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
    private String timestamp;
    private String subcategory;  // Community 엔티티의 Subcategory 열거형을 사용

    public CommunityDTO(Community community) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = community.getTimestamp().format(formatter);
        this.userId = community.getUser().getId();
        this.composer = community.getComposer();
        this.content = community.getContent();
        this.subcategory = community.getSubcategory().name();  // Subcategory 열거형의 이름을 문자열로 변환
    }

    @Override
    public String toString() {
        return "CommunityDTO{" +
                "userId=" + userId +
                ", composer='" + composer + '\'' +
                ", content='" + content + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", subcategory='" + subcategory + '\'' +
                '}';
    }
}