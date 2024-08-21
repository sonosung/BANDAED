package org.astrologist.midea.repository;

import org.astrologist.midea.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    List<Community> findBySubcategoryOrderByTimestampAsc(Community.Subcategory subcategory);
}