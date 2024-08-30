package org.astrologist.midea.repository;

import org.astrologist.midea.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MideaLikeRepository extends JpaRepository<MideaLike, Long> {
    Optional<MideaLike> findByUserAndPost1(User user, Community post);
    boolean existsByUserAndPost1(User user, Community post);

    Optional<MideaLike> findByUserAndPost2(User user, Mindlist post); // 좋아요를 찾는 메서드
    boolean existsByUserAndPost2(User user, Mindlist post); // 좋아요 여부 확인 메서드
    long countByPost2(Mindlist post); // 게시물의 총 좋아요 수를 반환하는 메서드

    Optional<MideaLike> findByUserAndPost3(User user, MindlistAdmin post); // 좋아요를 찾는 메서드
    boolean existsByUserAndPost3(User user, MindlistAdmin post); // 좋아요 여부 확인 메서드
    long countByPost3(MindlistAdmin post); // 게시물의 총 좋아요 수를 반환하는 메서드
}