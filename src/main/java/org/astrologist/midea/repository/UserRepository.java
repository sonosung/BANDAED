package org.astrologist.midea.repository;

import org.astrologist.midea.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @NonNull
    Optional<User> findByEmail(@NonNull String email);

    @NonNull
    Optional<User> findByNickname(@NonNull String nickname);

    @NonNull
    Optional<User> findById(@NonNull Long id);

    @NonNull
    User findByEmailAndNickname(@NonNull String email, @NonNull String nickname);

    // 현재 사용자의 감정 상태와 유사한 사용자 찾기
    @Query("SELECT u FROM User u WHERE " +
            "u.happy = :happy AND " +
            "u.sad = :sad AND " +
            "u.calm = :calm AND " +
            "u.stressed = :stressed AND " +
            "u.joyful = :joyful AND " +
            "u.energetic = :energetic AND " +
            "u.id <> :currentUserId")
    List<User> findUsersWithSameEmotions(@Param("happy") boolean happy,
                                         @Param("sad") boolean sad,
                                         @Param("calm") boolean calm,
                                         @Param("stressed") boolean stressed,
                                         @Param("joyful") boolean joyful,
                                         @Param("energetic") boolean energetic,
                                         @Param("currentUserId") Long currentUserId);
}