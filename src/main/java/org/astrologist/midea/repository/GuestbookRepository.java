package org.astrologist.midea.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.astrologist.midea.entity.Guestbook;

public interface GuestbookRepository extends JpaRepository<Guestbook, Long> ,
        QuerydslPredicateExecutor<Guestbook>{


}
