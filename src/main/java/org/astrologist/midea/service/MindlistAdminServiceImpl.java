package org.astrologist.midea.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.astrologist.midea.dto.*;
import org.astrologist.midea.entity.*;
import org.astrologist.midea.repository.CommentRepository;
import org.astrologist.midea.repository.MideaLikeRepository;
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
public class MindlistAdminServiceImpl implements MindlistAdminService {

    private final MindlistAdminRepository repository; //반드시 final로 선언.

    private final CommentRepository commentRepository;

    private final MideaLikeRepository mideaLikeRepository; /*좋아요 레포지토리*/

    //글쓰기
    @Override
    public Long register(MindlistAdminDTO dto) {
        log.info("-------------MindlistAdminServiceImpl register() 실행--------------");
        log.info(dto);

        MindlistAdmin mindlistAdmin = dtoToEntity(dto);
        log.info(mindlistAdmin);

        repository.save(mindlistAdmin);

        return mindlistAdmin.getMno();
    }

    //리스트 조회
    @Override
    public PageResultDTO<MindlistAdminDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        log.info(pageRequestDTO);

        Function<Object[], MindlistAdminDTO> fn = (en -> entityToDto((MindlistAdmin) en[0], (User) en[1], (Long) en[2]));

        Page<Object[]> result = repository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("mno").descending()));


        return new PageResultDTO<>(result, fn);
    }

    // 좋아요 상태 포함 리스트 조회 메서드
    @Override
    public PageResultDTO<MindlistAdminDTO, Object[]> getListWithLikes(PageRequestDTO pageRequestDTO, User currentUser) {
        log.info(pageRequestDTO);

        Page<Object[]> result = repository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("mno").descending())
        );

        // 좋아요 상태 추가
        Function<Object[], MindlistAdminDTO> fn = (en -> {
            MindlistAdmin mindlistAdmin = (MindlistAdmin) en[0];
            User user = (User) en[1];
            Long count = (Long) en[2];

            MindlistAdminDTO dto = entityToDto(mindlistAdmin, user, count);

            // 좋아요 상태 설정
            boolean liked = mideaLikeRepository.existsByUserAndPost3(currentUser, mindlistAdmin);
            log.info("Post mno=" + mindlistAdmin.getMno() + " liked by user " + currentUser.getNickname() + ": " + liked);
            dto.setLiked(liked);

            return dto;
        });

        return new PageResultDTO<>(result, fn);
    }

    //알고리즘 리스트 조회
    @Override
    public AlgorithmResultDTO<MindlistAdminDTO, Object[]> getAlgorithmList(AlgorithmRequestDTO algorithmRequestDTO) {

        log.info(algorithmRequestDTO);

//        Function<Object[], MindlistDTO> fn = (en -> entityToDTO((Mindlist)en[0],(User)en[1],(Long)en[2],(Long)en[3]));
        Function<Object[], MindlistAdminDTO> fn = (en -> entityToDto((MindlistAdmin) en[0], (User) en[1], (Long) en[2]));
//        Page<Object[]> result = repository.getBoardWithReplyCount(
//                pageRequestDTO.getPageable(Sort.by("bno").descending())  );
        Page<Object[]> algorithm = repository.searchPage(
                algorithmRequestDTO.getType(),
                algorithmRequestDTO.getKeyword(),
                algorithmRequestDTO.getPageable(Sort.by("mno").descending()));


        return new AlgorithmResultDTO<>(algorithm, fn);
    }

    //상세페이지 조회
    @Override
    public MindlistAdminDTO read(Long mno) {

        Object result = repository.getMindlistAdminByMno(mno);

        Object[] arr = (Object[]) result;

        return entityToDto((MindlistAdmin) arr[0], (User) arr[1], (Long) arr[2]);
    }

    //삭제
    @Transactional
    @Override
    public void removeWithComments(Long mno) {

        //댓글 부터 삭제
        commentRepository.deleteMAByMno(mno);

        repository.deleteById(mno);

    }

    //수정
    @Transactional
    @Override
    public void modify(MindlistAdminDTO mindlistAdminDTO) {

        Optional<MindlistAdmin> result = repository.findById(mindlistAdminDTO.getMno());

        if (result.isPresent()) {

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

    @Override
    @Transactional
    public void toggleLike(Long mno, User user) {
        log.info("Attempting to toggle like for mno=" + mno + " by user=" + user.getNickname());

        MindlistAdmin post = repository.findById(mno)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        Optional<MideaLike> existingLike = mideaLikeRepository.findByUserAndPost3(user, post);

        if (existingLike.isPresent()) {
            log.info("Like exists. Deleting like for mno=" + mno + " by user=" + user.getNickname());
            mideaLikeRepository.delete(existingLike.get());
        } else {
            log.info("Like does not exist. Adding like for mno=" + mno + " by user=" + user.getNickname());
            MideaLike newLike = new MideaLike();
            newLike.setUser(user);
            newLike.setPost3(post);
            mideaLikeRepository.save(newLike);
        }

        long likeCount = mideaLikeRepository.countByPost3(post);
        post.updateLikeCount((int) likeCount);
        repository.save(post);

        log.info("Updated like count for mno=" + mno + " to " + likeCount);
    }

    @Override
    public boolean checkUserLiked(Long mno, User currentUser) {
        log.info("Checking if user=" + currentUser.getNickname() + " liked post mno=" + mno);

        MindlistAdmin post = repository.findById(mno)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        boolean exists = mideaLikeRepository.existsByUserAndPost3(currentUser, post);
        log.info("User " + currentUser.getNickname() + " liked status for mno=" + mno + ": " + exists);

        return exists;

/*//    public Mindlist getMindlist(Long mno){
//        Optional<MindlistAdmin> mindlistAdmin = this.repository.findById(mno);
//        if(mindlistAdmin.isPresent()) {
//            MindlistAdmin mindlistAdmin11 = mindlistAdmin.get();
//            mindlistAdmin11.setViewCount(mindlistAdmin11.getViewCount()+1);
//        }
//    }*/

    }
}
