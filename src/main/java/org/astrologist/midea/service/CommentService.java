package org.astrologist.midea.service;

import org.astrologist.midea.dto.CommentDTO;
import org.astrologist.midea.entity.Comment;
import org.astrologist.midea.entity.Mindlist;
import org.astrologist.midea.entity.MindlistAdmin;
import org.astrologist.midea.entity.User;

import java.util.List;

public interface CommentService {

    /*------------------------------------------ ↓ 유저 게시판 댓글 CRUD ------------------------------------------------*/

    // 등록
    Long register(CommentDTO commentDTO);

    // 목록
    List<CommentDTO> getList(Long mno);

    // 수정
    void modify(CommentDTO commentDTO);

    // 삭제
    void remove(Long cno);

    //CommentDTO를 Comment 객체로 변환 Mindlist객체의 처리가 수반됨.
    default Comment dtotoEntity(CommentDTO commentDTO){

        Mindlist mindlist = Mindlist.builder().mno(commentDTO.getMno()).build();

        User user= User.builder()
                .nickname(commentDTO.getCommenter())
                .build();

        Comment comment = Comment.builder()
                .cno(commentDTO.getCno())
                .text(commentDTO.getText())
                .commenter(user)
//                .commenter(commentDTO.getCommenter())
                .mindlist(mindlist)
                .build();

        return comment;
    }

    //Comment 객체를 CommentDTO로 변환 Mindlist 객체가 필요하지 않으므로, 게시물 번호만
    default CommentDTO entityToDTO(Comment comment){

        CommentDTO dto = CommentDTO.builder()
                .cno(comment.getCno())
                .text(comment.getText())
                .commenter(String.valueOf(comment.getCommenter()))
                .regDate(comment.getRegDate())
                .modDate(comment.getModDate())
                .build();

        return dto;
    }

    /*----------------------------------------- ↓ 관리자 게시판 댓글 CRUD -----------------------------------------------*/

//    // 등록
//    Long registerToMa(CommentDTO commentDTO);
//
//    // 목록
//    List<CommentDTO> getMaList(Long mno);
//
//    // 수정
//    void modifyMa(CommentDTO commentDTO);
//
//    // 삭제
//    void removeMa(Long cno);
//
//    //Comment 객체를 CommentDTO로 변환 Mindlist 객체가 필요하지 않으므로, 게시물 번호만
//    default CommentDTO maEntityToDTO(Comment comment){
//
//        //UserDTO에서 게시판에 보여주고 싶은 객체를 가져와서 게시판 dto로 보냄.
//        User user = User.builder()
//                .email(comment.getEmail())
//                .nickname(comment.getNickname())
//                .build();
//
//        CommentDTO dto = CommentDTO.builder()
//                .cno(comment.getCno())
//                .text(comment.getText())
//                .commenter(comment.getCommenter())
//                .regDate(comment.getRegDate())
//                .modDate(comment.getModDate())
//                .build();
//
//        return dto;
//    }
//
//    //CommentDTO를 Comment 객체로 변환 MindlistAdmin객체의 처리가 수반됨.
//    default Comment dtotoMaEntity(CommentDTO commentDTO){
//
//        MindlistAdmin mindlistAdmin = MindlistAdmin.builder().mno(commentDTO.getMno()).build();
//
//        Comment comment = Comment.builder()
//                .cno(commentDTO.getCno())
//                .text(commentDTO.getText())
//                .commenter(commentDTO.getCommenter())
//                .mindlistAdmin(mindlistAdmin)
//                .build();
//
//        return comment;
//    }
}
