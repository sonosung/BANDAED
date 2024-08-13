package org.astrologist.midea.repository;

import org.astrologist.midea.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface MyPageUploadRepository extends JpaRepository<User, Long> {
/*닉네임추가*/
    @NonNull
    Optional<User> findByEmail(@NonNull String email);

    @NonNull
    Optional<User> findById(@NonNull Long id);

    @NonNull
    Optional<User> findByNickname(@NonNull String nickname);
}