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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Controller
@CrossOrigin(origins = "http://your-client-domain.com")
@RequestMapping("/midea/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final Map<Long, String> activeUsers = new ConcurrentHashMap<>(); // 활성 사용자 목록
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
        logger.info("createCommunityPost 메서드 호출됨");

        User user = (User) session.getAttribute("user");

        if (user != null) {
            logger.info("세션에서 가져온 사용자 ID {}", user.getId());
        } else {
            logger.warn("세션에 사용자가 없습니다.");
            return ResponseEntity.status(403).body(null);
        }

        communityDTO.setUserId(user.getId());
        logger.info("DTO 내용 {}", communityDTO);

        try {
            Community newPost = communityService.createCommunityPost(communityDTO);
            if (newPost == null) {
                return ResponseEntity.status(409).body(null); // 중복된 게시물이 있을 경우
            }
            logger.info("게시글이 성공적으로 저장되었습니다. ID {}", newPost.getId());

            // SSE 클라이언트들에게 메시지 전송
            notifyClients(newPost);
            return ResponseEntity.ok(newPost.getId());
        } catch (Exception e) {
            logger.error("게시글 저장 중 오류 발생", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{postId}")
    @ResponseBody
    public ResponseEntity<String> updatePost(@PathVariable Long postId, @RequestBody Map<String, String> payload, HttpSession session) {
        User user = (User) session.getAttribute("user");

        logger.info("Update Request - Session User ID {}, Post ID {}", user != null ? user.getId() : "null", postId);

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
            logger.error("게시글 수정 실패", e);
            return ResponseEntity.status(500).body("게시글 수정 실패: " + e.getMessage());
        }
    }

    @DeleteMapping("/{postId}")
    @ResponseBody
    public ResponseEntity<String> deletePost(@PathVariable Long postId, HttpSession session) {
        User user = (User) session.getAttribute("user");

        logger.info("Delete Request - Session User ID {}, Post ID {}", user != null ? user.getId() : "null", postId);

        if (user == null) {
            return ResponseEntity.status(403).body("로그인이 필요합니다.");
        }

        try {
            communityService.deletePost(postId, user);
            return ResponseEntity.ok("게시글이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            logger.error("게시글 삭제 실패", e);
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
            logger.error("좋아요 상태 변경 실패", e);
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
    public SseEmitter streamEvents(HttpSession session) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // 무제한 타임아웃 설정
        emitters.add(emitter);

        User user = (User) session.getAttribute("user");
        if (user != null) {
            activeUsers.put(user.getId(), user.getNickname());
            broadcastActiveUsers(); // 새로운 사용자 접속 시 업데이트
            logger.info("사용자 {}(ID: {}) SSE 연결 시작", user.getNickname(), user.getId());
        }

        // 연결 종료 시 호출할 메서드
        emitter.onCompletion(() -> cleanUpEmitter(emitter, user));
        emitter.onTimeout(() -> cleanUpEmitter(emitter, user));
        emitter.onError(e -> {
            cleanUpEmitter(emitter, user);
            logger.error("SSE 연결 오류: {}", e.getMessage());
        });

        return emitter;
    }

    @PostMapping("/disconnect")
    @ResponseBody
    public ResponseEntity<String> disconnect(@RequestBody Map<String, Long> payload) {
        Long userId = payload.get("userId");
        if (userId != null && activeUsers.containsKey(userId)) {
            activeUsers.remove(userId);
            broadcastActiveUsers(); // 사용자 연결 종료 시 업데이트
            logger.info("사용자 ID {} SSE 연결 종료 요청", userId);
            return ResponseEntity.ok("Disconnected successfully.");
        }
        return ResponseEntity.status(400).body("User not found or already disconnected.");
    }

    private void cleanUpEmitter(SseEmitter emitter, User user) {
        emitters.remove(emitter);
        if (user != null) {
            activeUsers.remove(user.getId());
            logger.info("사용자 {}(ID: {}) SSE 연결 종료", user.getNickname(), user.getId());
            broadcastActiveUsers(); // 사용자 연결 종료 시 업데이트
        }
    }

    private void broadcastActiveUsers() {
        List<String> activeUserList = new ArrayList<>(activeUsers.values());
        logger.info("활성 사용자 목록: {}", activeUserList);

        SseEmitter.SseEventBuilder event = SseEmitter.event()
                .name("activeUsers")
                .data(activeUserList);

        List<SseEmitter> deadEmitters = new ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(event);
            } catch (IOException e) {
                deadEmitters.add(emitter);
                logger.warn("클라이언트로 이벤트 전송 중 오류 발생: {}", e.getMessage());
            }
        }
        emitters.removeAll(deadEmitters);
    }

    private void notifyClients(Community community) {
        SseEmitter.SseEventBuilder event = SseEmitter.event()
                .name("newCommunityPost")
                .data(community);

        List<SseEmitter> deadEmitters = new ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(event);
                logger.info("SSE 이벤트 전송 성공: {}", community);
            } catch (IOException e) {
                // 클라이언트 연결 끊김 처리
                deadEmitters.add(emitter);
                logger.warn("클라이언트 연결 끊김으로 Emitter 제거: {}", e.getMessage());
            }
        }

        // 끊긴 emitter를 제거
        emitters.removeAll(deadEmitters);
    }
}