package org.astrologist.midea.repository;

import org.astrologist.midea.entity.Community;
import org.astrologist.midea.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    List<Community> findBySubcategoryOrderByTimestampAsc(Community.Subcategory subcategory);

    boolean existsByUserAndContentAndSubcategoryAndId(User user, String content, Community.Subcategory subcategory, Long id);

}