package org.astrologist.midea.service;

import org.astrologist.midea.dto.MindlistDTO;
import org.astrologist.midea.dto.PageRequestDTO;
import org.astrologist.midea.dto.PageResultDTO;
import org.astrologist.midea.entity.Mindlist;

public interface MindlistService {
    Long register(MindlistDTO dto);

    PageResultDTO<MindlistDTO, Mindlist> getList(PageRequestDTO requestDTO);

    MindlistDTO read(Long mno);

    void remove(Long mno);

    void modify(MindlistDTO dto);

    default Mindlist dtoToEntity(MindlistDTO dto) {
        Mindlist entity = Mindlist.builder()
                .mno(dto.getMno())
                .composer(dto.getComposer())
                .title(dto.getTitle())
                .url(dto.getUrl())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }

    default MindlistDTO entityToDto(Mindlist entity){

        MindlistDTO dto = MindlistDTO.builder()
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

