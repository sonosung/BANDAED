package org.astrologist.midea.repository;

import org.astrologist.midea.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    // 필요한 경우, 커스텀 쿼리 메서드를 정의할 수 있습니다.
}
