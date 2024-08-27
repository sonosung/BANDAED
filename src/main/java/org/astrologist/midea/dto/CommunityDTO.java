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
    private Long id;
    private Long userId;
    private String composer;
    private String content;
    private String timestamp;
    private String subcategory;

    public CommunityDTO(Community community) {
        this.id = community.getId();
        this.userId = community.getUser().getId();
        this.composer = community.getComposer();
        this.content = community.getContent();
        this.timestamp = community.getTimestamp() != null ? community.getTimestamp().toString() : "Timestamp not available";  // null 체크
        this.subcategory = community.getSubcategory().name();
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
