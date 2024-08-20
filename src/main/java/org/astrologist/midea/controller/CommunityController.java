package org.astrologist.midea.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.astrologist.midea.dto.CommunityDTO;
import org.astrologist.midea.entity.Community;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.service.CommunityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/midea/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    /**
     * 커뮤니티 페이지를 표시합니다.
     * 게시글 목록과 새로운 게시글을 작성하기 위한 폼을 모델에 추가합니다.
     *
     * @param model   - 모델 객체
     * @param session - 세션 객체
     * @return community/community 템플릿
     */
    @GetMapping
    public String getCommunityPage(Model model, HttpSession session) {
        // 세션에서 로그인된 사용자 정보를 가져오기
        User user = (User) session.getAttribute("user");

        // 사용자 정보가 있으면 닉네임과 프로필 이미지를 모델에 추가, 없으면 기본값 설정
        if (user != null) {
            model.addAttribute("username", user.getNickname());
            model.addAttribute("userRole", user.getUserRole().name());

            // 프로필 이미지 경로가 없을 경우 기본 이미지를 설정
            String profileImagePath = user.getProfileImagePath();
            if (profileImagePath == null || profileImagePath.isEmpty()) {
                profileImagePath = "/default.images/default-profile.jpg";
            }
            model.addAttribute("profileImage", profileImagePath);
        } else {
            model.addAttribute("username", "Guest");
            model.addAttribute("userRole", "GUEST");
            model.addAttribute("profileImage", "/default.images/default-profile.jpg");
        }

        // 커뮤니티 게시글 목록 추가
        List<Community> communityList = communityService.getAllCommunities();
        model.addAttribute("communities", communityList);

        // 새로운 게시글 작성을 위한 DTO 객체 추가
        model.addAttribute("communityDTO", new CommunityDTO());

        return "community/community";  // community.html 템플릿을 렌더링
    }
}