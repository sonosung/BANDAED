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
    private Long id; // 게시물의 ID 추가
    private Long userId;
    private String composer;
    private String content;
    private String timestamp;
    private String subcategory;  // Community 엔티티의 Subcategory 열거형을 사용

    public CommunityDTO(Community community) {
        this.id = community.getId(); // 엔티티에서 ID를 가져옴
        this.timestamp = community.getTimestamp().toString();
        this.userId = community.getUser().getId();
        this.composer = community.getComposer();
        this.content = community.getContent();
        this.subcategory = community.getSubcategory().name();  // Subcategory 열거형의 이름을 문자열로 변환
    }

    @Override
    public String toString() {
        return "CommunityDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", composer='" + composer + '\'' +
                ", content='" + content + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", subcategory='" + subcategory + '\'' +
                '}';
    }
}