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

    //한개의 row(Object) 내에 Object[]로 나옴.
    @Query("select ma, u from MindlistAdmin ma left join ma.userIdx u where ma.mno =:mno")
    Object getMindlistAdminWithWriter(@Param("mno") Long mno);

    @Query("SELECT ma, c FROM MindlistAdmin ma LEFT JOIN Comment c ON c.mindlistAdmin = ma WHERE ma.mno = :mno")
    List<Object[]> getMindlistAdminWithComment(@Param("mno") Long mno);

    @Query(value ="SELECT ma, u, count(c) " +
            " FROM MindlistAdmin ma " +
            " LEFT JOIN ma.userIdx u " +
            " LEFT JOIN Comment c ON c.mindlistAdmin = ma " +
            " GROUP BY ma",
            countQuery ="SELECT count(ma) FROM MindlistAdmin ma")
    Page<Object[]> getMindlistAdminWithCommentCount(Pageable pageable);


    @Query("SELECT ma, u, count(c) " +
            " FROM MindlistAdmin ma LEFT JOIN ma.userIdx u " +
            " LEFT OUTER JOIN Comment c ON c.mindlistAdmin = ma" +
            " WHERE ma.mno = :mno")
    Object getMindlistAdminByMno(@Param("mno") Long mno);
}
