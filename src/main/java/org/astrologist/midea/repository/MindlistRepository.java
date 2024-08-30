package org.astrologist.midea.repository;

import org.astrologist.midea.entity.Mindlist;
import org.astrologist.midea.repository.search.SearchMindlistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MindlistRepository extends JpaRepository<Mindlist, Long>, SearchMindlistRepository {

    //한개의 row(Object) 내에 Object[]로 나옴.
    @Query("select m, u from Mindlist m left join m.userIdx u where m.mno =:mno")
    Object getMindlistWithWriter(@Param("mno") Long mno);

    @Query("SELECT m, c FROM Mindlist m LEFT JOIN Comment c ON c.mindlist = m WHERE m.mno = :mno")
    List<Object[]> getMindlistWithComment(@Param("mno") Long mno);

    @Query(value ="SELECT m, u, count(c) " +
            " FROM Mindlist m " +
            " LEFT JOIN m.userIdx u " +
            " LEFT JOIN Comment c ON c.mindlist = m " +
            " GROUP BY m",
            countQuery ="SELECT count(m) FROM Mindlist m")
    Page<Object[]> getMindlistWithCommentCount(Pageable pageable);


    @Query("SELECT m, u, count(c) " +
            " FROM Mindlist m LEFT JOIN m.userIdx u " +
            " LEFT OUTER JOIN Comment c ON c.mindlist = m" +
            " WHERE m.mno = :mno")
    Object getMindlistByMno(@Param("mno") Long mno);

    @Query("SELECT m, u, count(v) " +
            " FROM Mindlist m LEFT JOIN m.userIdx u " +
            " LEFT OUTER JOIN View v ON v.mindlist = m" +
            " WHERE m.mno = :mno")
    Object getViewCountByMno(@Param("mno") Long mno);
}
