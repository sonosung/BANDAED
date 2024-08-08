package org.astrologist.midea.repository;

import org.astrologist.midea.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPageRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
