package org.astrologist.midea.repository;

import org.astrologist.midea.entity.Comment;
import org.astrologist.midea.entity.Mindlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>{

    //mindlist 게시글 삭제시에 댓글들 삭제.
    @Modifying
    @Query("delete from Comment c where c.mindlist.mno =:mno ")
    void deleteByMno(Long mno);

    //게시글로 댓글 목록 가져오기
    List<Comment> getCommentByMindlistOrderByCno(Mindlist mindlist);

}
