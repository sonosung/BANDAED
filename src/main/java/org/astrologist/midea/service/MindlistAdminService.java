package org.astrologist.midea.service;

import org.astrologist.midea.dto.MindlistAdminDTO;
import org.astrologist.midea.dto.PageRequestDTO;
import org.astrologist.midea.dto.PageResultDTO;
import org.astrologist.midea.dto.UserPageDTO;
import org.astrologist.midea.entity.MindlistAdmin;
import org.astrologist.midea.entity.User;

public interface MindlistAdminService {

    Long register(MindlistAdminDTO mindlistAdminDTO);

    PageResultDTO<MindlistAdminDTO, MindlistAdmin> getList(PageRequestDTO requestDTO);

    MindlistAdminDTO read(Long mno);

    void remove(Long mno);

    void modify(MindlistAdminDTO dto);

    default MindlistAdmin dtoToEntity(MindlistAdminDTO dto) {
        MindlistAdmin entity = MindlistAdmin.builder()
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
                .build();
        return entity;
    }

    default MindlistAdminDTO entityToDto(MindlistAdmin entity) {

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
                .build();

        return mindlistAdminDTO;
    }

}


