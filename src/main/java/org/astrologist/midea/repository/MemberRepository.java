package org.astrologist.midea.repository;

import org.astrologist.midea.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

//    @NonNull
//    Optional<Member> findByEmail(@NonNull String email);
//
//    @NonNull
//    Optional<Member> findByNickname(@NonNull String nickname);
//
//    @NonNull
//    Optional<Member> findById(@NonNull Long id);
//
//    @NonNull
//    Member findByEmailAndNickname(@NonNull String email, @NonNull String nickname);
}