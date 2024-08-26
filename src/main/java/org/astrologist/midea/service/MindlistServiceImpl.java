package org.astrologist.midea.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.astrologist.midea.dto.MindlistDTO;
import org.astrologist.midea.dto.PageRequestDTO;
import org.astrologist.midea.dto.PageResultDTO;
import org.astrologist.midea.entity.Mindlist;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.repository.CommentRepository;
import org.astrologist.midea.repository.MindlistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MindlistServiceImpl implements MindlistService {

    private final MindlistRepository repository; //반드시 final로 선언.

    private final CommentRepository commentRepository;

    //글쓰기
    @Override
    public Long register(MindlistDTO dto) {


        log.info("-------------MindlistServiceImpl register() 실행--------------");
        log.info(dto);

        Mindlist mindlist = dtoToEntity(dto);

        repository.save(mindlist);

        return mindlist.getMno();
    }

    //리스트 조회
    @Override
    public PageResultDTO<MindlistDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        log.info(pageRequestDTO);

//        Function<Object[], MindlistDTO> fn = (en -> entityToDTO((Mindlist)en[0],(User)en[1],(Long)en[2],(Long)en[3]));
        Function<Object[], MindlistDTO> fn = (en -> entityToDTO((Mindlist)en[0],(User)en[1],(Long)en[2]));
//        Page<Object[]> result = repository.getBoardWithReplyCount(
//                pageRequestDTO.getPageable(Sort.by("bno").descending())  );
        Page<Object[]> result = repository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("mno").descending())  );


        return new PageResultDTO<>(result, fn);
    }

    //상세페이지 조회
    @Override
    public MindlistDTO read(Long mno) {

        Object result = repository.getMindlistByMno(mno);

        Object[] arr = (Object[])result;

        return entityToDTO((Mindlist)arr[0], (User)arr[1], (Long)arr[2]);
    }

    //삭제
    @Transactional
    @Override
    public void removeWithComments(Long mno) {

        //댓글 부터 삭제
        commentRepository.deleteByMno(mno);

        repository.deleteById(mno);

    }

    //수정
    @Transactional
    @Override
    public void modify(MindlistDTO mindlistDTO) {

        Mindlist mindlist = repository.getReferenceById(mindlistDTO.getMno());

        if(mindlist != null) {

            mindlist.changeComposer(mindlistDTO.getComposer());
            mindlist.changeContent(mindlistDTO.getContent());
            mindlist.changeTitle(mindlistDTO.getTitle());
            mindlist.changeUrl(mindlistDTO.getUrl());
            mindlist.changeHappy(mindlistDTO.isHappy());
            mindlist.changeSad(mindlistDTO.isSad());
            mindlist.changeCalm(mindlistDTO.isCalm());
            mindlist.changeStressed(mindlistDTO.isStressed());
            mindlist.changeJoyful(mindlistDTO.isJoyful());
            mindlist.changeEnergetic(mindlistDTO.isEnergetic());

            repository.save(mindlist);
        }
    }

    @Transactional
    public Mindlist getViewByMindlistOrderByCno(Long mno) {
        Optional<Mindlist> mindlist = this.repository.findById(mno);
        if (mindlist.isPresent()) {
            Mindlist mindlist1 = mindlist.get();
            mindlist1.setViewCount(mindlist1.getViewCount() + 1);
            this.repository.save(mindlist1);
            return mindlist1;

        }
        return null;
    }

}
