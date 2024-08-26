package org.astrologist.midea.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.astrologist.midea.dto.CommunityDTO;
import org.astrologist.midea.dto.MideaLikeDTO;
import org.astrologist.midea.entity.Community;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.service.CommunityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/midea/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(CommunityController.class);

    @GetMapping
    public String getCommunityPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user != null) {
            populateUserAttributes(model, user);
        } else {
            populateGuestAttributes(model);
        }

        List<Community> communityList = communityService.getAllCommunities();
        model.addAttribute("communities", communityList);

        model.addAttribute("communityDTO", new CommunityDTO());

        return "community/community";  // community.html 템플릿을 렌더링
    }

    private void populateUserAttributes(Model model, User user) {
        model.addAttribute("username", user.getNickname());
        model.addAttribute("userRole", user.getUserRole().name());
        model.addAttribute("userId", user.getId());

        String profileImagePath = user.getProfileImagePath();
        if (profileImagePath == null || profileImagePath.isEmpty()) {
            profileImagePath = "/default.images/default-profile.jpg";
        }
        model.addAttribute("profileImage", profileImagePath);
    }

    private void populateGuestAttributes(Model model) {
        model.addAttribute("username", "Guest");
        model.addAttribute("userRole", "GUEST");
        model.addAttribute("profileImage", "/default.images/default-profile.jpg");
    }

    /*@GetMapping("/subcategory/{subcategory}")
    @ResponseBody
    public ResponseEntity<List<Community>> getCommunitiesBySubcategory(@PathVariable String subcategory) {
        try {
            Community.Subcategory subcategoryEnum = Community.Subcategory.valueOf(subcategory);
            List<Community> communities = communityService.getCommunitiesBySubcategory(subcategoryEnum);
            return ResponseEntity.ok(communities);
        } catch (IllegalArgumentException e) {
            logError("Invalid subcategory", subcategory, e);
            return ResponseEntity.badRequest().body(null);
        }
    }*/

    @GetMapping("/subcategory/{subcategory}")
    @ResponseBody
    public ResponseEntity<List<CommunityDTO>> getCommunitiesBySubcategory(@PathVariable String subcategory) {
        try {
            Community.Subcategory subcategoryEnum = Community.Subcategory.valueOf(subcategory);
            List<Community> communities = communityService.getCommunitiesBySubcategory(subcategoryEnum);

            List<CommunityDTO> communityDTOs = communities.stream()
                    .map(CommunityDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(communityDTOs);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid subcategory: {}", subcategory, e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Long> createCommunityPost(@RequestBody CommunityDTO communityDTO, HttpSession session) {
        logInfo("createCommunityPost 메서드 호출됨");

        User user = (User) session.getAttribute("user");

        if (user != null) {
            logInfo("세션에서 가져온 사용자 ID", user.getId());
        } else {
            logWarning("세션에 사용자가 없습니다.");
            return ResponseEntity.status(403).body(null);
        }

        communityDTO.setUserId(user.getId());
        logInfo("DTO 내용", communityDTO);

        try {
            Community newPost = communityService.createCommunityPost(communityDTO);
            if (newPost == null) {
                return ResponseEntity.status(409).body(null); // 중복된 게시물이 있을 경우
            }
            logInfo("게시글이 성공적으로 저장되었습니다. ID", newPost.getId());

            // SSE 클라이언트들에게 메시지 전송
            notifyClients(newPost);
            return ResponseEntity.ok(newPost.getId());
        } catch (Exception e) {
            logError("게시글 저장 중 오류 발생", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{postId}")
    @ResponseBody
    public ResponseEntity<String> updatePost(@PathVariable Long postId, @RequestBody Map<String, String> payload, HttpSession session) {
        User user = (User) session.getAttribute("user");

        logInfo("Update Request - Session User ID", user != null ? user.getId() : "null", postId);

        if (user == null) {
            return ResponseEntity.status(403).body("로그인이 필요합니다.");
        }

        String newContent = payload.get("content");
        if (newContent == null || newContent.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("수정할 내용이 필요합니다.");
        }

        try {
            communityService.updatePost(postId, user, newContent);
            return ResponseEntity.ok("게시글이 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            logError("게시글 수정 실패", e);
            return ResponseEntity.status(500).body("게시글 수정 실패: " + e.getMessage());
        }
    }

    @DeleteMapping("/{postId}")
    @ResponseBody
    public ResponseEntity<String> deletePost(@PathVariable Long postId, HttpSession session) {
        User user = (User) session.getAttribute("user");

        logInfo("Delete Request - Session User ID", user != null ? user.getId() : "null", postId);

        if (user == null) {
            return ResponseEntity.status(403).body("로그인이 필요합니다.");
        }

        try {
            communityService.deletePost(postId, user);
            return ResponseEntity.ok("게시글이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            logError("게시글 삭제 실패", e);
            return ResponseEntity.status(500).body("게시글 삭제 실패: " + e.getMessage());
        }
    }

    @PostMapping("/like")
    @ResponseBody
    public ResponseEntity<String> toggleLike(@RequestBody MideaLikeDTO mideaLikeDTO, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(403).body("로그인이 필요합니다.");
        }

        try {
            communityService.toggleLike(mideaLikeDTO, user);
            return ResponseEntity.ok("좋아요 상태가 변경되었습니다.");
        } catch (Exception e) {
            logError("좋아요 상태 변경 실패", e);
            return ResponseEntity.status(500).body("좋아요 상태 변경 실패: " + e.getMessage());
        }
    }

    @GetMapping("/like-status/{postId}")
    @ResponseBody
    public ResponseEntity<Boolean> getLikeStatus(@PathVariable Long postId, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(403).body(false);
        }

        boolean isLiked = communityService.isPostLikedByUser(postId, user);
        return ResponseEntity.ok(isLiked);
    }

    @GetMapping("/sse")
    public SseEmitter streamEvents() {
        SseEmitter emitter = new SseEmitter(0L); // 무제한 타임아웃 설정
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        return emitter;
    }

    private void notifyClients(Community newPost) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        logger.info("Notifying {} clients about new post with ID: {}", emitters.size(), newPost.getId());

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("newPost")
                        .data(new CommunityDTO(newPost), MediaType.APPLICATION_JSON));
                logger.info("Successfully notified client for post ID: {}", newPost.getId());
            } catch (IOException e) {
                logger.error("Failed to notify client for post ID: {}. Removing emitter.", newPost.getId(), e);
                deadEmitters.add(emitter);
            }
        }
        if (!deadEmitters.isEmpty()) {
            emitters.removeAll(deadEmitters);
            logger.info("Removed {} dead emitters.", deadEmitters.size());
        }
    }

    // 공통 로그 메시지 출력 메서드
    private void logInfo(String message, Object... args) {
        logger.info(message, args);
    }

    private void logWarning(String message, Object... args) {
        logger.warn(message, args);
    }

    private void logError(String message, Object... args) {
        logger.error(message, args);
    }
}
