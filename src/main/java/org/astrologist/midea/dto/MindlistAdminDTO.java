package org.astrologist.midea.dto;

import lombok.*;
import org.astrologist.midea.entity.User;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MindlistAdminDTO {

    private Long mno;
    private String composer;
    private String title;
    private String content;
    private String url;
    private String email;
    private LocalDateTime regDate, modDate;
    private int commentCount; //해당 게시글의 댓글 수

    private boolean happy;
    private boolean sad;
    private boolean calm;
    private boolean stressed;
    private boolean joyful;
    private boolean energetic;

}

