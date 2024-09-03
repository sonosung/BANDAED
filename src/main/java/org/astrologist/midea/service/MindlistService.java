package org.astrologist.midea.service;

import org.astrologist.midea.dto.*;
import org.astrologist.midea.entity.Mindlist;
import org.astrologist.midea.entity.User;


public interface MindlistService {

    String extractYouTubeVideoID(String url);
    Long register(MindlistDTO dto);

    PageResultDTO<MindlistDTO, Object[]> getList(PageRequestDTO requestDTO);

    AlgorithmResultDTO<MindlistDTO, Object[]> getAlgorithmList(AlgorithmRequestDTO algorithmRequestDTO);

    //조회수 같이 구현
    MindlistDTO read(Long mno);

    void removeWithComments(Long mno);

    void modify(MindlistDTO mindlistDTO);

    default Mindlist dtoToEntity(MindlistDTO dto) {

        // Extract the video ID from the URL
        String videoID = extractYouTubeVideoID(dto.getUrl());

        //UserDTO에서 게시판에 보여주고 싶은 객체를 가져와서 게시판 dto로 보냄.
        User user = User.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .build();

        Mindlist mindlist = Mindlist.builder()
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
                .viewCount((long) dto.getViewCount())
                .build();

        return mindlist;
    }

    default MindlistDTO entityToDTO(Mindlist mindlist, User user, Long commentCount, Long viewCount){

        MindlistDTO mindlistDTO = MindlistDTO.builder()
                .mno(mindlist.getMno())
                .composer(mindlist.getComposer())
                .title(mindlist.getTitle())
                .url(mindlist.getUrl())
                .nickname(mindlist.getNickname())
                .calm(mindlist.isCalm())
                .happy(mindlist.isHappy())
                .joyful(mindlist.isJoyful())
                .energetic(mindlist.isEnergetic())
                .sad(mindlist.isSad())
                .stressed(mindlist.isStressed())
                .likeCount(mindlist.getLikeCount())
                .commentCount(commentCount.intValue())
                .viewCount(viewCount.intValue())
                .content(mindlist.getContent())
                .regDate(mindlist.getRegDate())
                .modDate(mindlist.getModDate())
                .build();

        return mindlistDTO;
    }

    void toggleLike(Long mno, User user);/*좋아요*/

    boolean checkUserLiked(Long mno, User currentUser); /*특정 사용자가 좋아요 확인*/

    PageResultDTO<MindlistDTO, Object[]> getListWithLikes(PageRequestDTO pageRequestDTO, User currentUser);
}