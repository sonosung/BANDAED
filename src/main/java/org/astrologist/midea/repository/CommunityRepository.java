package org.astrologist.midea.repository;

import org.astrologist.midea.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    // 특정 주제에 속한 채팅 메시지를 시간 순서대로 조회합니다.
    List<Community> findBySubcategoryAndIsChatMessageTrueOrderByTimestampAsc(Community.Subcategory subcategory);
}
