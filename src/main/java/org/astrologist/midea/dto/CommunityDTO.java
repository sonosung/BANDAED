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
    private Long id;
    private Long userId;
    private String composer;
    private String content;
    private String timestamp;
    private String subcategory;
    private int likeCount;

    public CommunityDTO(Community community) {
        this.id = community.getId();
        this.userId = community.getUser().getId();
        this.composer = community.getComposer();
        this.content = community.getContent();
        this.subcategory = community.getSubcategory().name();
        this.likeCount = community.getLikeCount();  // 좋아요 수 초기화

        // 시간 형식을 "HH:MM:SS"로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.timestamp = community.getTimestamp() != null
                ? community.getTimestamp().format(formatter)
                : "Timestamp not available";
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
                ", likeCount=" + likeCount +  // 새로운 필드 추가
                '}';
    }
}
