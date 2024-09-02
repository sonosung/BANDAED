package org.astrologist.midea.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.astrologist.midea.dto.*;
import org.astrologist.midea.entity.MideaLike;
import org.astrologist.midea.entity.Mindlist;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.repository.CommentRepository;
import org.astrologist.midea.repository.MideaLikeRepository;
import org.astrologist.midea.repository.MindlistRepository;
import org.astrologist.midea.repository.ViewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MindlistServiceImpl implements MindlistService {

    private final MindlistRepository repository; //반드시 final로 선언.

    private final CommentRepository commentRepository;

    private final MideaLikeRepository mideaLikeRepository; /*좋아요 레포지토리*/

    private final ViewRepository viewRepository;

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

//        Function<Object[], MindlistDTO> fn = (en -> entityToDTO((Mindlist)en[0],(User)en[1],(Long)en[2], (Long) en[3]));
        Function<Object[], MindlistDTO> fn = (en -> entityToDTO((Mindlist)en[0],(User)en[1],(Long)en[2]));
//        Page<Object[]> result = repository.getBoardWithReplyCount(
//                pageRequestDTO.getPageable(Sort.by("bno").descending())  );
        Page<Object[]> result = repository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("mno").descending())  );


        return new PageResultDTO<>(result, fn);
    }

    @Override
    public void viewCountValidation(Mindlist mindlist, HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        boolean isCookie = false;

        // request에 쿠키들이 있을 때
        for (int i = 0; cookies != null && i < cookies.length; i++) {
            // postView 쿠키가 있을 때
            if (cookies[i].getName().equals("postView")) {
                // cookie 변수에 저장
                cookie = cookies[i];
                // 만약 cookie 값에 현재 게시글 번호가 없을 때
                if (!cookie.getValue().contains("[" + mindlist.getMno() + "]")) {
                    // 해당 게시글 조회수를 증가시키고, 쿠키 값에 해당 게시글 번호를 추가
                    mindlist.addViewCount();
                    cookie.setValue(cookie.getValue() + "[" + mindlist.getMno() + "]");
                }
                isCookie = true;
                break;
            }
        }

        // 만약 postView라는 쿠키가 없으면 처음 접속한 것이므로 새로 생성
        if (!isCookie) {
            mindlist.addViewCount();
            cookie = new Cookie("viewCount", "[" + mindlist.getMno() + "]"); // oldCookie에 새 쿠키 생성
        }

        log.info("cookie: " + cookie);

        // 쿠키 유지시간을 오늘 하루 자정까지로 설정
        long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        cookie.setPath("/"); // 모든 경로에서 접근 가능
        cookie.setMaxAge((int) (todayEndSecond - currentSecond));
        response.addCookie(cookie);

    }

    //알고리즘 리스트 조회
    // 좋아요 상태 포함 리스트 조회 메서드
    @Override
    public PageResultDTO<MindlistDTO, Object[]> getListWithLikes(PageRequestDTO pageRequestDTO, User currentUser) {
        log.info(pageRequestDTO);

        Page<Object[]> result = repository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("mno").descending())
        );

        // 좋아요 상태 추가
        Function<Object[], MindlistDTO> fn = (en -> {
            Mindlist mindlist = (Mindlist) en[0];
            User user = (User) en[1];
            Long count = (Long) en[2];

            MindlistDTO dto = entityToDTO(mindlist, user, count);

            // 좋아요 상태 설정
            boolean liked = mideaLikeRepository.existsByUserAndPost2(currentUser, mindlist);
            dto.setLiked(liked);

            return dto;
        });

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public AlgorithmResultDTO<MindlistDTO, Object[]> getAlgorithmList(AlgorithmRequestDTO algorithmRequestDTO) {

        log.info(algorithmRequestDTO);

//        Function<Object[], MindlistDTO> fn = (en -> entityToDTO((Mindlist)en[0],(User)en[1],(Long)en[2],(Long)en[3]));
        Function<Object[], MindlistDTO> fn = (en -> entityToDTO((Mindlist)en[0],(User)en[1],(Long)en[2]));
//        Page<Object[]> result = repository.getBoardWithReplyCount(
//                pageRequestDTO.getPageable(Sort.by("bno").descending())  );
        Page<Object[]> algorithm = repository.searchPage(
                algorithmRequestDTO.getType(),
                algorithmRequestDTO.getKeyword(),
                algorithmRequestDTO.getPageable(Sort.by("mno").descending())  );


        return new AlgorithmResultDTO<>(algorithm, fn);
    }

    //상세페이지 조회
    @Override
    public MindlistDTO read(Long mno/*, Mindlist mindlist, HttpServletRequest request, HttpServletResponse response*/) {

        Object result = repository.getMindlistByMno(mno);

        Object[] arr = (Object[])result;
//
//        Cookie[] cookies = request.getCookies();
//        Cookie cookie = null;
//        boolean isCookie = false;
//
//        // request에 쿠키들이 있을 때
//        for (int i = 0; cookies != null && i < cookies.length; i++) {
//            // postView 쿠키가 있을 때
//            if (cookies[i].getName().equals("postView")) {
//                // cookie 변수에 저장
//                cookie = cookies[i];
//                // 만약 cookie 값에 현재 게시글 번호가 없을 때
//                if (!cookie.getValue().contains("[" + mindlist.getMno() + "]")) {
//                    // 해당 게시글 조회수를 증가시키고, 쿠키 값에 해당 게시글 번호를 추가
//                    mindlist.addViewCount();
//                    cookie.setValue(cookie.getValue() + "[" + mindlist.getMno() + "]");
//                }
//                isCookie = true;
//                break;
//            }
//        }
//
//        // 만약 postView라는 쿠키가 없으면 처음 접속한 것이므로 새로 생성
//        if (!isCookie) {
//            mindlist.addViewCount();
//            cookie = new Cookie("viewCount", "[" + mindlist.getMno() + "]"); // oldCookie에 새 쿠키 생성
//        }
//
//        log.info("cookie: " + cookie);
//
//        // 쿠키 유지시간을 오늘 하루 자정까지로 설정
//        long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
//        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
//        cookie.setPath("/"); // 모든 경로에서 접근 가능
//        cookie.setMaxAge((int) (todayEndSecond - currentSecond));
//        response.addCookie(cookie);

//        return entityToDTO((Mindlist)arr[0], (User)arr[1], (Long)arr[2], (Long)arr[3]);
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

    @Override
    @Transactional
    public void toggleLike(Long mno, User user) {
        log.info("Attempting to toggle like for mno=" + mno + " by user=" + user.getNickname());

        Mindlist post = repository.findById(mno)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        Optional<MideaLike> existingLike = mideaLikeRepository.findByUserAndPost2(user, post);

        if (existingLike.isPresent()) {
            log.info("Like exists. Deleting like for mno=" + mno + " by user=" + user.getNickname());
            mideaLikeRepository.delete(existingLike.get());
        } else {
            log.info("Like does not exist. Adding like for mno=" + mno + " by user=" + user.getNickname());
            MideaLike newLike = new MideaLike();
            newLike.setUser(user);
            newLike.setPost2(post);
            mideaLikeRepository.save(newLike);
        }

        long likeCount = mideaLikeRepository.countByPost2(post);
        post.updateLikeCount((int) likeCount);
        repository.save(post);

        log.info("Updated like count for mno=" + mno + " to " + likeCount);
    }

    @Override
    public boolean checkUserLiked(Long mno, User currentUser) {
        log.info("Checking if user=" + currentUser.getNickname() + " liked post mno=" + mno);

        Mindlist post = repository.findById(mno)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        boolean exists = mideaLikeRepository.existsByUserAndPost2(currentUser, post);
        log.info("User " + currentUser.getNickname() + " liked status for mno=" + mno + ": " + exists);

        return exists;
    }

}
