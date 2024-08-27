package org.astrologist.midea.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
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
    private Long userIdx;
    private String email;
    private String nickname;
    private LocalDateTime regDate, modDate;
    private int commentCount; //해당 게시글의 댓글 수
    private int likeCount; //게시물의 좋아요 수
    private int viewCount; //게시물의 조회수

    private boolean happy;
    private boolean sad;
    private boolean calm;
    private boolean stressed;
    private boolean joyful;
    private boolean energetic;

}
