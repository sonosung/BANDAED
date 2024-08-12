package org.astrologist.midea.repository;

import org.astrologist.midea.entity.MindlistAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MindlistAdminRepository extends JpaRepository<MindlistAdmin, Long>,
        QuerydslPredicateExecutor<MindlistAdmin> {
}
