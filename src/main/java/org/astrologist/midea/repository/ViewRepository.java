package org.astrologist.midea.repository;

import jakarta.servlet.http.HttpServletRequest;
import org.astrologist.midea.dto.ViewDTO;
import org.astrologist.midea.entity.Comment;
import org.astrologist.midea.entity.Mindlist;
import org.astrologist.midea.entity.MindlistAdmin;
import org.astrologist.midea.entity.View;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViewRepository extends JpaRepository<View, Long> {

    // 유저게시글로 조회 목록 가져오기
    List<View> getViewByMindlistOrderByVno(Mindlist mindlist);

    //관리자 게시글로 조회 목록 가져오기
    List<View> getViewByMindlistAdminOrderByVno(MindlistAdmin mindlistAdmin);

}
