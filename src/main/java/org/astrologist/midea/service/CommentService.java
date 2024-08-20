package org.astrologist.midea.service;

import org.astrologist.midea.dto.CommentDTO;
import org.astrologist.midea.entity.Comment;
import org.astrologist.midea.entity.Mindlist;

import java.util.List;

public interface CommentService {

    Long register(CommentDTO commentDTO); //댓글 등록

    List<CommentDTO> getList(Long mno);

    void modify(CommentDTO commentDTO);

    void remove(Long cno);

    //CommentDTO를 Comment 객체로 변환 Mindlist객체의 처리가 수반됨.
    default Comment dtotoEntity(CommentDTO commentDTO){

        Mindlist mindlist = Mindlist.builder().mno(commentDTO.getMno()).build();

        Comment comment = Comment.builder()
                .cno(commentDTO.getCno())
                .text(commentDTO.getText())
                .commenter(commentDTO.getCommenter())
                .mindlist(mindlist)
                .build();

        return comment;
    }

    //Comment 객체를 CommentDTO로 변환 Mindlist 객체가 필요하지 않으므로, 게시물 번호만
    default CommentDTO entityToDTO(Comment comment){
        CommentDTO dto = CommentDTO.builder()
                .cno(comment.getCno())
                .text(comment.getText())
                .commenter(comment.getCommenter())
                .redDate(comment.getRegDate())
                .modDate(comment.getModDate())
                .build();

        return dto;
    }
}
