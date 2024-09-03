package org.astrologist.midea.service;

import org.astrologist.midea.dto.*;
import org.astrologist.midea.entity.MindlistAdmin;
import org.astrologist.midea.entity.User;


public interface MindlistAdminService {

    String extractYouTubeVideoID(String url);
    Long register(MindlistAdminDTO dto);

    PageResultDTO<MindlistAdminDTO, Object[]> getList(PageRequestDTO requestDTO);

    AlgorithmResultDTO<MindlistAdminDTO, Object[]> getAlgorithmList(AlgorithmRequestDTO algorithmRequestDTO);

    //조회수 같이 구현
    MindlistAdminDTO read(Long mno);

    void removeWithComments(Long mno);

    void modify(MindlistAdminDTO mindlistAdminDTO);

    default MindlistAdmin dtoToEntity(MindlistAdminDTO dto) {

        // Extract the video ID from the URL
        String videoID = extractYouTubeVideoID(dto.getUrl());

        //UserDTO에서 게시판에 보여주고 싶은 객체를 가져와서 게시판 dto로 보냄.
        User user = User.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .build();

        MindlistAdmin mindlistAdmin = MindlistAdmin.builder()
                .mno(dto.getMno())
                .composer(dto.getComposer())
                .title(dto.getTitle())
                .url(dto.getUrl())
                .content(dto.getContent())
                .nickname(dto.getNickname())
                .calm(dto.isCalm())
                .happy(dto.isHappy())
                .joyful(dto.isJoyful())
                .energetic(dto.isEnergetic())
                .sad(dto.isSad())
                .stressed(dto.isStressed())
                .likeCount(dto.getLikeCount())
                .commentCount(dto.getCommentCount())
                .viewCount(dto.getViewCount())
                .build();

        return mindlistAdmin;
    }

    default MindlistAdminDTO entityToDTO(MindlistAdmin mindlistAdmin, User user, Long commentCount){

        MindlistAdminDTO mindlistAdminDTO = MindlistAdminDTO.builder()
                .mno(mindlistAdmin.getMno())
                .composer(mindlistAdmin.getComposer())
                .title(mindlistAdmin.getTitle())
                .url(mindlistAdmin.getUrl())
                .nickname(mindlistAdmin.getNickname())
                .calm(mindlistAdmin.isCalm())
                .happy(mindlistAdmin.isHappy())
                .joyful(mindlistAdmin.isJoyful())
                .energetic(mindlistAdmin.isEnergetic())
                .sad(mindlistAdmin.isSad())
                .stressed(mindlistAdmin.isStressed())
                .likeCount(mindlistAdmin.getLikeCount())
                .commentCount(commentCount.intValue())
                .viewCount(mindlistAdmin.getViewCount())
                .content(mindlistAdmin.getContent())
                .regDate(mindlistAdmin.getRegDate())
                .modDate(mindlistAdmin.getModDate())
                .build();

        return mindlistAdminDTO;
    }

    void toggleLike(Long mno, User user);/*좋아요*/

    boolean checkUserLiked(Long mno, User currentUser); /*특정 사용자가 좋아요 확인*/

    PageResultDTO<MindlistAdminDTO, Object[]> getListWithLikes(PageRequestDTO pageRequestDTO, User currentUser);
}