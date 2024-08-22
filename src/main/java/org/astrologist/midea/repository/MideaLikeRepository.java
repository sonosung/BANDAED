package org.astrologist.midea.repository;

import org.astrologist.midea.entity.Community;
import org.astrologist.midea.entity.MideaLike;
import org.astrologist.midea.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MideaLikeRepository extends JpaRepository<MideaLike, Long> {
    Optional<MideaLike> findByUserAndPost1(User user, Community post);
}