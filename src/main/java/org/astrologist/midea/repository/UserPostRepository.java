package org.astrologist.midea.repository;

import org.astrologist.midea.entity.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPostRepository extends JpaRepository<UserPost, Long> {
}
