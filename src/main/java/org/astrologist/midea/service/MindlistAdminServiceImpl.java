package org.astrologist.midea.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.astrologist.midea.dto.MindlistAdminDTO;
import org.astrologist.midea.dto.MindlistDTO;
import org.astrologist.midea.dto.PageRequestDTO;
import org.astrologist.midea.dto.PageResultDTO;
import org.astrologist.midea.entity.*;
import org.astrologist.midea.repository.CommentRepository;
import org.astrologist.midea.repository.MindlistAdminRepository;
import org.astrologist.midea.repository.MindlistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MindlistAdminServiceImpl implements MindlistAdminService{

    private final MindlistAdminRepository repository; //반드시 final로 선언.

    private final CommentRepository commentRepository;

    @Override
    public Long register(MindlistAdminDTO dto) {
        log.info("dto---------------------");
        log.info(dto);

        MindlistAdmin mindlistAdmin = dtoToEntity(dto);
        log.info(mindlistAdmin);

        repository.save(mindlistAdmin);

        return mindlistAdmin.getMno();
    }

    @Override
    public MindlistAdminDTO read(Long mno) {

        Object result = repository.getMindlistAdminByMno(mno);

        Object[] arr = (Object[])result;

        return entityToDto((MindlistAdmin) arr[0], (User)arr[1], (Long)arr[2]);
    }

    @Override
    public PageResultDTO<MindlistAdminDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        log.info(pageRequestDTO);

        Function<Object[], MindlistAdminDTO> fn = (en -> entityToDto((MindlistAdmin)en[0],(User)en[1],(Long)en[2]));

        Page<Object[]> result = repository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("mno").descending())  );


        return new PageResultDTO<>(result, fn);
    }

    @Transactional
    @Override
    public void removeWithComments(Long mno) {

        //댓글 부터 삭제
        commentRepository.deleteByMno(mno);

        repository.deleteById(mno);

    }

    @Override
    public void modify(MindlistAdminDTO mindlistAdminDTO) {
        Optional<MindlistAdmin> result = repository.findById(mindlistAdminDTO.getMno());

        if(result.isPresent()){

            MindlistAdmin entity = result.get();

            entity.changeTitle(mindlistAdminDTO.getTitle());
            entity.changeUrl(mindlistAdminDTO.getUrl());
            entity.changeComposer(mindlistAdminDTO.getComposer());
            entity.changeContent(mindlistAdminDTO.getContent());
            entity.changeHappy(mindlistAdminDTO.isHappy());
            entity.changeSad(mindlistAdminDTO.isSad());
            entity.changeCalm(mindlistAdminDTO.isCalm());
            entity.changeStressed(mindlistAdminDTO.isStressed());
            entity.changeJoyful(mindlistAdminDTO.isJoyful());
            entity.changeEnergetic(mindlistAdminDTO.isEnergetic());

            repository.save(entity);
        }

    }

}
