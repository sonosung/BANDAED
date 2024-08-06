package org.astrologist.midea.repository;


import org.astrologist.midea.entity.Mindlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MindlistRepository extends JpaRepository<Mindlist, Long>,
        QuerydslPredicateExecutor<Mindlist> {

}
