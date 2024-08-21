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

    @GetMapping
    public String getCommunityPage(Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user != null) {
            model.addAttribute("username", user.getNickname());
            model.addAttribute("userRole", user.getUserRole().name());
            model.addAttribute("userId", user.getId());

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

        List<Community> communityList = communityService.getAllCommunities();
        model.addAttribute("communities", communityList);

        model.addAttribute("communityDTO", new CommunityDTO());

        return "community/community";  // community.html 템플릿을 렌더링
    }

    @PostMapping
    @ResponseBody
    public String createCommunityPost(@RequestBody CommunityDTO communityDTO, HttpSession session) {

        System.out.println("createCommunityPost 메서드 호출됨");

        User user = (User) session.getAttribute("user");


        if (user != null) {
            System.out.println("세션에서 가져온 사용자 ID: " + user.getId());
        } else {
            System.out.println("세션에 사용자가 없습니다.");
        }

        if (user == null) {
            return "로그인이 필요합니다.";
        }

        communityDTO.setUserId(user.getId());

        System.out.println("DTO 내용: " + communityDTO.toString());

        try {
            communityService.createCommunityPost(communityDTO);
            System.out.println("게시글이 성공적으로 저장되었습니다.");
            return "게시글이 성공적으로 저장되었습니다.";
        } catch (Exception e) {
            System.err.println("게시글 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();  // 오류 스택 트레이스 출력
            return "게시글 저장 중 오류가 발생했습니다.";
        }
    }
}
