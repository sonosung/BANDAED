package org.astrologist.midea.service;

import org.astrologist.midea.dto.MindlistDTO;
import org.astrologist.midea.dto.PageRequestDTO;
import org.astrologist.midea.dto.PageResultDTO;
import org.astrologist.midea.entity.Mindlist;
import org.astrologist.midea.entity.User;

public interface MindlistService {

    Long register(MindlistDTO dto);

    PageResultDTO<MindlistDTO, Object[]> getList(PageRequestDTO requestDTO);

    MindlistDTO read(Long mno);

    void removeWithComments(Long mno);

    void modify(MindlistDTO mindlistDTO);

    default Mindlist dtoToEntity(MindlistDTO dto) {

        //UserDTO에서 게시판에 보여주고 싶은 객체를 가져와서 게시판 dto로 보냄.
        User user = User.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
//                .password(dto.getPassword())
                .build();

        Mindlist mindlist = Mindlist.builder()
                .mno(dto.getMno())
                .composer(dto.getComposer())
                .title(dto.getTitle())
                .url(dto.getUrl())
                .content(dto.getContent())
//                .email(user)
                .nickname(dto.getNickname())
//                .password(user)
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
        return mindlist;
    }

    default MindlistDTO entityToDTO(Mindlist mindlist, User user, Long commentCount/*, Long viewCount*/){

        MindlistDTO mindlistDTO = MindlistDTO.builder()
                .mno(mindlist.getMno())
                .composer(mindlist.getComposer())
                .title(mindlist.getTitle())
                .url(mindlist.getUrl())
//                .email(user.getNickname())
//                .nickname(user.getNickname())
                .nickname(mindlist.getNickname())
                .likeCount(mindlist.getLikeCount())
                .commentCount(commentCount.intValue())
//                .viewCount(viewCount.intValue())
                .content(mindlist.getContent())
                .regDate(mindlist.getRegDate())
                .modDate(mindlist.getModDate())
                .build();

        return mindlistDTO;
    }

}