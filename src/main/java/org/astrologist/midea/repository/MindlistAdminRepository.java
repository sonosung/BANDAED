package org.astrologist.midea.repository;

import org.astrologist.midea.entity.MindlistAdmin;
import org.astrologist.midea.repository.search.SearchMindlistAdminRepository;
import org.astrologist.midea.repository.search.SearchMindlistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MindlistAdminRepository extends JpaRepository<MindlistAdmin, Long>, SearchMindlistAdminRepository {

    @Query("select ma, e from MindlistAdmin ma left join ma.email e where ma.mno =:mno")
    Object getMindlistAdminWithWriter(@Param("mno") Long mno);

    @Query("SELECT ma, c FROM MindlistAdmin ma LEFT JOIN Comment c ON c.mindlistAdmin = ma WHERE ma.mno = :mno")
    List<Object[]> getMindlistAdminWithReply(@Param("mno") Long mno);

//    @Query(value ="SELECT ma, e, count(c) " +
//            " FROM MindlistAdmin ma " +
//            " LEFT JOIN ma.email e " +
//            " LEFT JOIN Comment c ON c.mindlistAdmin = ma " +
//            " GROUP BY ma",
//            countQuery ="SELECT count(ma) FROM MindlistAdmin ma")
//    Page<Object[]> getMindlistAdminWithCommentCount(Pageable pageable);


    @Query("SELECT ma, e, count(c) " +
            " FROM MindlistAdmin ma LEFT JOIN ma.email e " +
            " LEFT OUTER JOIN Comment c ON c.mindlistAdmin = ma" +
            " WHERE ma.mno = :mno")
    Object getMindlistAdminByMno(@Param("mno") Long mno);
}
