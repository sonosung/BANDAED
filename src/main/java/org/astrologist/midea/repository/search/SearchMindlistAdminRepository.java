package org.astrologist.midea.repository.search;

import org.astrologist.midea.entity.MindlistAdmin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchMindlistAdminRepository {

    MindlistAdmin search1();

    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);
}
