package org.astrologist.midea.service;

import org.astrologist.midea.dto.*;
import org.astrologist.midea.entity.MindlistAdmin;
import org.astrologist.midea.entity.User;

public interface MindlistAdminService {

    Long register(MindlistAdminDTO dto);

    PageResultDTO<MindlistAdminDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

    AlgorithmResultDTO<MindlistAdminDTO, Object[]> getAlgorithmList(AlgorithmRequestDTO algorithmRequestDTO);

    MindlistAdminDTO read(Long mno);

    void removeWithComments(Long mno);

    void modify(MindlistAdminDTO dto);

    //게시물 등록을 위해 MindlistAdminDTO를 MindlistAdmin 엔티티 타입으로 변환하기 위한 처리
    default MindlistAdmin dtoToEntity(MindlistAdminDTO dto) {

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
//                .email(user)
                .nickname(dto.getNickname())
                .calm(dto.isCalm())
                .happy(dto.isHappy())
                .joyful(dto.isJoyful())
                .energetic(dto.isEnergetic())
                .sad(dto.isSad())
                .stressed(dto.isStressed())
                .likeCount(dto.getLikeCount())
                .commentCount(dto.getCommentCount())
                .build();
        return mindlistAdmin;
    }

    default MindlistAdminDTO entityToDto(MindlistAdmin entity, User user, Long commentCount) {

        MindlistAdminDTO mindlistAdminDTO = MindlistAdminDTO.builder()
                .mno(entity.getMno())
                .composer(entity.getComposer())
                .title(entity.getTitle())
                .url(entity.getUrl())
                .content(entity.getContent())
                .nickname(entity.getNickname())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .calm(entity.isCalm())
                .happy(entity.isHappy())
                .joyful(entity.isJoyful())
                .energetic(entity.isEnergetic())
                .sad(entity.isSad())
                .stressed(entity.isStressed())
                .commentCount(commentCount.intValue())
                .build();

        return mindlistAdminDTO;
    }

}


