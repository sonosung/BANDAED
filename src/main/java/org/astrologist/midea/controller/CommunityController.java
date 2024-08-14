package org.astrologist.midea.controller;

import lombok.RequiredArgsConstructor;
import org.astrologist.midea.dto.CommunityDTO;
import org.astrologist.midea.entity.Community;
import org.astrologist.midea.service.CommunityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/midea/community")
@RequiredArgsConstructor

public class CommunityController {

    private final CommunityService communityService;

    @GetMapping
    public String getCommunityPage(Model model) {
        List<Community> communityList = communityService.getAllCommunities();
        model.addAttribute("communities", communityList);
        model.addAttribute("communityDTO", new CommunityDTO());  // 폼에 사용될 DTO 객체를 모델에 추가
        return "community/community";  // community.html 템플릿을 렌더링합니다.
    }

    @PostMapping("/chat")
    public String createCommunityPost(@ModelAttribute CommunityDTO communityDTO) {
        communityService.createCommunityPost(communityDTO);
        return "redirect:/midea/community";  // 게시물을 생성한 후 커뮤니티 페이지로 리디렉션합니다.
    }
}