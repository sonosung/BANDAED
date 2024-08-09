package org.astrologist.midea.repository;

import org.astrologist.midea.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface UserPageRepository extends JpaRepository<User, Long> {

    @NonNull
    Optional<User> findByEmail(@NonNull String email);

    @NonNull
    Optional<User> findById(@NonNull Long id);
}