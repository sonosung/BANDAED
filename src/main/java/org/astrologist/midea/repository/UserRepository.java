package org.astrologist.midea.repository;

import org.astrologist.midea.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);//이메일 중복검사
    Optional<User> findByNickname(String nickname);//닉네임 중복검사
}
