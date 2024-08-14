package org.astrologist.midea.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MindlistDTO {

    private Long mno;
    private String composer;
    private String title;
    private String content;
    private String url;
    private String nickname;
    private String profileImagePath;
    private LocalDateTime regDate, modDate;

    private boolean happy;
    private boolean sad;
    private boolean calm;
    private boolean stressed;
    private boolean joyful;
    private boolean energetic;

}
