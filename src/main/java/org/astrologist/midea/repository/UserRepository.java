package org.astrologist.midea.repository;

import org.astrologist.midea.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);

    // 이메일과 닉네임으로 사용자를 찾는 메서드 추가
    User findByEmailAndNickname(String email, String nickname);
}
