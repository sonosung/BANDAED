package org.astrologist.midea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.astrologist.midea.entity.Community;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDTO {
    private Long userId;
    private String composer;
    private String content;
    private Community.Subcategory subcategory;  // Community 엔티티의 Subcategory 열거형을 사용
}