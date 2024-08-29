package org.astrologist.midea.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.astrologist.midea.dto.CommentDTO;
import org.astrologist.midea.entity.Comment;
import org.astrologist.midea.entity.Mindlist;
import org.astrologist.midea.entity.MindlistAdmin;
import org.astrologist.midea.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;

    /*----------------------------------------- ↓ 유저 게시판 댓글 CRUD -----------------------------------------------*/
    //등록
    @Override
    public Long register(CommentDTO commentDTO) {

        Comment comment = dtotoEntity(commentDTO);
        commentRepository.save(comment);
        return comment.getCno();
    }

    //목록
    @Override
    public List<CommentDTO> getList(Long mno) {

        List<Comment> result = commentRepository.getCommentByMindlistOrderByCno(Mindlist.builder().mno(mno).build());

        return result.stream().map(comment -> entityToDTO(comment)).collect(Collectors.toList());
    }

    //수정
    @Override
    public void modify(CommentDTO commentDTO) {

        Comment comment = dtotoEntity(commentDTO);
        commentRepository.save(comment);
    }

    //삭제
    @Override
    public void remove(Long cno) {

        commentRepository.deleteById(cno);
    }

    /*----------------------------------------- ↓ 관리자 게시판 댓글 CRUD -----------------------------------------------*/
    //등록
//    @Override
//    public Long registerToMa(CommentDTO commentDTO) {
//
//        Comment comment = dtotoEntity(commentDTO);
//        commentRepository.save(comment);
//        return comment.getCno();
//    }
//
//    //목록
//    @Override
//    public List<CommentDTO> getMaList(Long mno) {
//
//        List<Comment> result = commentRepository.getCommentByMindlistAdminOrderByCno(MindlistAdmin.builder().mno(mno).build());
//        return result.stream().map(comment -> maEntityToDTO(comment)).collect(Collectors.toList());
//    }
//
//    //수정
//    @Override
//    public void modifyMa(CommentDTO commentDTO) {
//
//        Comment comment = dtotoMaEntity(commentDTO);
//        commentRepository.save(comment);
//    }
//
//    //삭제
//    @Override
//    public void removeMa(Long cno) {
//
//        commentRepository.deleteById(cno);
//    }

}
