package org.astrologist.midea.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MindlistDTO {

    private Long mno;
    private String composer;
    private String title;
    private String url;
    private String writer;
    private LocalDateTime regDate, modDate;

}
