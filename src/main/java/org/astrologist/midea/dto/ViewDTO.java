package org.astrologist.midea.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class ViewDTO {

    private Long vno;                       //조회 넘버
    private String viewUser;                //조회자
    private Long aMno;                      //조회 관리자 게시글
    private Long uMno;                      //조회 유저 게시글
    private LocalDateTime regDate, modDate; //조회 날짜
}
