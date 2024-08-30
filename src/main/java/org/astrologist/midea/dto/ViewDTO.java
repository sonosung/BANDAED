package org.astrologist.midea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ViewDTO {

    private Long vno;
    private Long uId;
    private Long aMno;
    private Long uMno;
    private Long viewCount;
}
