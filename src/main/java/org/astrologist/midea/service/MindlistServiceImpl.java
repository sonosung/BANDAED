package org.astrologist.midea.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.astrologist.midea.dto.MindlistDTO;
import org.astrologist.midea.dto.PageRequestDTO;
import org.astrologist.midea.dto.PageResultDTO;
import org.astrologist.midea.entity.Mindlist;
import org.astrologist.midea.entity.QMindlist;
import org.astrologist.midea.repository.MindlistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MindlistServiceImpl implements MindlistService {

    private final MindlistRepository repository; //반드시 final로 선언.

    @Override
    public Long register(MindlistDTO dto) {
        log.info("DTO---------------------");
        log.info(dto);

        Mindlist entity = dtoToEntity(dto);

        log.info(entity);

        repository.save(entity);

        return entity.getMno();
    }

    @Override
    public PageResultDTO<MindlistDTO, Mindlist> getList(PageRequestDTO requestDTO){

        Pageable pageable = requestDTO.getPageable(Sort.by("mno").descending());

        BooleanBuilder booleanBuilder = getSearch(requestDTO); //검색 조건 처리

        Page<Mindlist> result = repository.findAll(booleanBuilder, pageable); //Querydsl 사용

        Function<Mindlist, MindlistDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);

    }

    @Override
    public MindlistDTO read(Long mno) {

        Optional<Mindlist> result = repository.findById(mno);

        return result.isPresent()? entityToDto(result.get()): null;
    }

    @Override
    public void remove(Long mno) {
        repository.deleteById(mno);
    }

    @Override
    public void modify(MindlistDTO dto) {
        Optional<Mindlist> result = repository.findById(dto.getMno());

        if(result.isPresent()){

            Mindlist entity = result.get();

            entity.changeComposer(dto.getComposer());
            entity.changeContent(dto.getContent());
            entity.changeTitle(dto.getTitle());
            entity.changeUrl(dto.getUrl());

            repository.save(entity);
        }

    }

    //Querydsl 처리
    private BooleanBuilder getSearch(PageRequestDTO requestDTO){
        String type = requestDTO.getType();

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QMindlist qMindlist = QMindlist.mindlist;

        String keyword = requestDTO.getKeyword();

        //mno가 0보다 큰 조건만 검색
        BooleanExpression expression = qMindlist.mno.gt(0L);

        booleanBuilder.and(expression);

        if (type == null || type.trim().length() == 0){
            return booleanBuilder;
        }

        //검색조건 작성하기
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("c")){
            conditionBuilder.or(qMindlist.composer.contains(keyword));
        }
        if(type.contains("t")){
            conditionBuilder.or(qMindlist.title.contains(keyword));
        }
        if(type.contains("w")){
            conditionBuilder.or(qMindlist.writer.contains(keyword));
        }

        //모든 조건 통합
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }

}
