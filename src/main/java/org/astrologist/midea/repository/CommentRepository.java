package org.astrologist.midea.repository;

import org.astrologist.midea.entity.Comment;
import org.astrologist.midea.entity.Mindlist;
import org.astrologist.midea.entity.MindlistAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>{

    /*----------------------------------------- ↓ 유저 게시판 댓글 -----------------------------------------------*/

    //게시글 삭제시에 댓글들 삭제.
    @Modifying
    @Query("delete from Comment c where c.mindlist.mno =:mno ")
    void deleteByMno(Long mno);

    //댓글 목록
    List<Comment> getCommentByMindlistOrderByCno(Mindlist mindlist);

    /*----------------------------------------- ↓ 관리자 게시판 댓글 -----------------------------------------------*/

    //게시글 삭제시에 댓글들 삭제.
    @Modifying
    @Query("delete from Comment c where c.mindlistAdmin.mno =:mno ")
    void deleteMAByMno(Long mno);

    //댓글 목록
    List<Comment> getCommentByMindlistAdminOrderByCno(MindlistAdmin mindlistAdmin);

}
