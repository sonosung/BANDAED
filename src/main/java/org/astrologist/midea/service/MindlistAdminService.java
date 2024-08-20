package org.astrologist.midea.service;

import org.astrologist.midea.dto.MindlistAdminDTO;
import org.astrologist.midea.dto.PageRequestDTO;
import org.astrologist.midea.dto.PageResultDTO;
import org.astrologist.midea.entity.MindlistAdmin;
import org.astrologist.midea.entity.User;

public interface MindlistAdminService {

    Long register(MindlistAdminDTO dto);

    PageResultDTO<MindlistAdminDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

    MindlistAdminDTO read(Long mno);

    void removeWithComments(Long mno);

    void modify(MindlistAdminDTO dto);

    default MindlistAdmin dtoToEntity(MindlistAdminDTO dto) {

        User user = User.builder().email(dto.getEmail()).build();

        MindlistAdmin entity = MindlistAdmin.builder()
                .mno(dto.getMno())
                .composer(dto.getComposer())
                .title(dto.getTitle())
                .url(dto.getUrl())
                .content(dto.getContent())
                .email(user)
                .calm(dto.isCalm())
                .happy(dto.isHappy())
                .joyful(dto.isJoyful())
                .energetic(dto.isEnergetic())
                .sad(dto.isSad())
                .stressed(dto.isStressed())
                .build();
        return entity;
    }

    default MindlistAdminDTO entityToDTO(MindlistAdmin entity, User user, Long commentCount) {

        MindlistAdminDTO mindlistAdminDTO = MindlistAdminDTO.builder()
                .mno(entity.getMno())
                .composer(entity.getComposer())
                .title(entity.getTitle())
                .url(entity.getUrl())
                .content(entity.getContent())
                .email(user.getEmail())
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


