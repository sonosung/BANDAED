package org.astrologist.midea.service;

import org.astrologist.midea.dto.MindlistAdminDTO;
import org.astrologist.midea.dto.PageRequestDTO;
import org.astrologist.midea.dto.PageResultDTO;
import org.astrologist.midea.entity.MindlistAdmin;

public interface MindlistAdminService {

    Long register(MindlistAdminDTO dto);

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
                .writer(dto.getWriter())
                .build();
        return entity;
    }

    default MindlistAdminDTO entityToDto(MindlistAdmin entity) {

        MindlistAdminDTO dto = MindlistAdminDTO.builder()
                .mno(entity.getMno())
                .composer(entity.getComposer())
                .title(entity.getTitle())
                .url(entity.getUrl())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();

        return dto;
    }

}


