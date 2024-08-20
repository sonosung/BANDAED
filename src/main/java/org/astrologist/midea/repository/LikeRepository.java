package org.astrologist.midea.repository;

import org.astrologist.midea.entity.Like;
import org.astrologist.midea.entity.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    // 특정 게시물의 좋아요 수를 계산
    long countByPost_Id(Long postId);
}
