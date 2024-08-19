package org.astrologist.midea.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.astrologist.midea.dto.CommunityDTO;
import org.astrologist.midea.entity.Community;
import org.astrologist.midea.service.CommunityService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.astrologist.midea.entity.User;

import java.time.LocalDateTime;
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
     * @param model - 모델 객체
     * @param session - 세션 객체
     * @return community/community 템플릿
     */
    @GetMapping
    public String getCommunityPage(Model model, HttpSession session) {
        // 세션에서 로그인된 사용자 정보를 가져오기
        User user = (User) session.getAttribute("user");

        // 로그인된 사용자가 있는지 확인
        if (user != null) {
            String username = user.getNickname();  // 닉네임 가져오기
            model.addAttribute("username", username);
        } else {
            model.addAttribute("username", "Guest");  // 로그인되지 않은 경우 기본 닉네임 설정
        }

        if (user != null) {
            // 닉네임과 프로필 이미지 경로를 모델에 추가
            model.addAttribute("username", user.getNickname());
            model.addAttribute("profileImage", user.getProfileImagePath());
        } else {
            // 사용자 정보가 없으면 기본값 설정
            model.addAttribute("username", "Guest");
            model.addAttribute("profileImage", "/default.images/default-profile.jpg");  // 기본 이미지 설정
        }

        // 커뮤니티 게시글 목록 추가
        List<Community> communityList = communityService.getAllCommunities();
        model.addAttribute("communities", communityList);

        // 새로운 게시글 작성을 위한 DTO 객체 추가
        model.addAttribute("communityDTO", new CommunityDTO());

        return "community/community";  // community.html 템플릿을 렌더링
    }

    /**
     * 새로운 게시글을 생성합니다.
     * 사용자가 작성한 데이터를 받아 게시글을 저장하고 커뮤니티 페이지로 리다이렉트합니다.
     *
     * @param communityDTO - 사용자가 작성한 게시글 데이터
     * @return 커뮤니티 페이지로 리다이렉트
     */
    @PostMapping
    public String createCommunityPost(@ModelAttribute CommunityDTO communityDTO) {
        communityService.createCommunityPost(communityDTO);
        return "redirect:/midea/community";  // 게시글 생성 후 커뮤니티 페이지로 리다이렉트
    }

    /**
     * WebSocket을 통해 수신된 채팅 메시지를 처리합니다.
     * 채팅 메시지를 저장하고, 해당 주제의 모든 클라이언트에게 메시지를 브로드캐스트합니다.
     *
     * @param chatMessage - 채팅 메시지 데이터
     * @return 저장된 채팅 메시지
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/{subcategory}")
    public Community sendMessage(@RequestBody Community chatMessage) {
        chatMessage.setIsChatMessage(true);  // 메시지가 채팅 메시지임을 표시
        chatMessage.setTimestamp(LocalDateTime.now());  // 메시지의 전송 시간 설정
        communityService.saveChatMessage(chatMessage);  // 메시지 저장
        return chatMessage;  // 저장된 메시지를 반환하여 클라이언트에 브로드캐스트
    }

    /**
     * 새로운 사용자가 채팅에 참여했을 때의 메시지를 처리합니다.
     *
     * @param chatMessage - 채팅 참여 메시지 데이터
     * @return 저장된 참여 메시지
     */
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/{subcategory}")
    public Community addUser(@RequestBody Community chatMessage) {
        chatMessage.setIsChatMessage(true);
        chatMessage.setContent(chatMessage.getUser().getNickname() + " has joined the chat");
        chatMessage.setTimestamp(LocalDateTime.now());
        communityService.saveChatMessage(chatMessage);
        return chatMessage;
    }

    /**
     * 특정 주제(Subcategory)에 대한 채팅 기록을 가져옵니다.
     *
     * @param subcategory - 조회할 주제
     * @return 해당 주제의 채팅 메시지 리스트
     */
    @GetMapping("/chat/history/{subcategory}")
    @ResponseBody
    public List<Community> getChatHistory(@PathVariable Community.Subcategory subcategory) {
        return communityService.getChatHistory(subcategory);
    }
}
