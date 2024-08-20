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

    @Query("select m, e from Mindlist m left join m.email e where m.mno =:mno")
    Object getMindlistdWithWriter(@Param("mno") Long mno);

    @Query("SELECT m, c FROM Mindlist m LEFT JOIN Comment c ON c.mindlist = m WHERE m.mno = :mno")
    List<Object[]> getMindlistWithComment(@Param("mno") Long mno);

    @Query(value ="SELECT m, e, count(c) " +
            " FROM Mindlist m " +
            " LEFT JOIN m.email e " +
            " LEFT JOIN Comment c ON c.mindlist = m " +
            " GROUP BY m",
            countQuery ="SELECT count(m) FROM Mindlist m")
    Page<Object[]> getMindlistWithCommentCount(Pageable pageable);


    @Query("SELECT m, e, count(c) " +
            " FROM Mindlist m LEFT JOIN m.email e " +
            " LEFT OUTER JOIN Comment c ON c.mindlist = m" +
            " WHERE m.mno = :mno")
    Object getMindlistByMno(@Param("mno") Long mno);
    
}
