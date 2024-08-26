package org.astrologist.midea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDTO {

    private Long cno;
    private String text;
    private String commenter;
    private Long mno; //게시글 번호
    private LocalDateTime regDate, modDate;
}
