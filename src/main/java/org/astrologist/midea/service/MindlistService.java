package org.astrologist.midea.service;

import org.astrologist.midea.dto.MindlistDTO;
import org.astrologist.midea.dto.PageRequestDTO;
import org.astrologist.midea.dto.PageResultDTO;
import org.astrologist.midea.entity.Member;
import org.astrologist.midea.entity.Mindlist;
import org.astrologist.midea.entity.User;

public interface MindlistService {
    Long register(MindlistDTO dto);

    PageResultDTO<MindlistDTO, Object[]> getList(PageRequestDTO requestDTO);

    MindlistDTO read(Long mno);

    void removeWithComments(Long mno);

    void modify(MindlistDTO dto);

    default Mindlist dtoToEntity(MindlistDTO mindlistDTO) {

        User user = User.builder().email(mindlistDTO.getEmail()).build();

        Mindlist mindlist = Mindlist.builder()
                .mno(mindlistDTO.getMno())
                .composer(mindlistDTO.getComposer())
                .title(mindlistDTO.getTitle())
                .url(mindlistDTO.getUrl())
                .content(mindlistDTO.getContent())
                .email(user)
                .nickname(mindlistDTO.getNickname())
                .build();
        return mindlist;
    }



    default MindlistDTO entityToDTO(Mindlist mindlist, User user, Long commentCount){

        MindlistDTO mindlistDTO = MindlistDTO.builder()
                .mno(mindlist.getMno())
                .composer(mindlist.getComposer())
                .title(mindlist.getTitle())
                .url(mindlist.getUrl())
                .email(user.getEmail())
                .nickname(mindlist.getNickname())
                .content(mindlist.getContent())
                .regDate(mindlist.getRegDate())
                .modDate(mindlist.getModDate())
                .build();

        return mindlistDTO;
    }

}

