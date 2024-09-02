package org.astrologist.midea.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.service.MindlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/midea")
@Log4j2
@RequiredArgsConstructor
public class DefaultController {

    private final MindlistService mindlistService; //MindlistService 인터페이스를 final로 구현.

    @Autowired
    private final HttpSession session;

    @GetMapping("/")
    public String index() {
        return "redirect:/midea/index";

    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        log.info("Logout......................");

        return "redirect:/midea/index";
    }

    @GetMapping({"/index", "/portfolio"})
    public void mindlist(Model model, HttpSession session) {
        // 세션에서 현재 로그인한 사용자 정보를 가져옵니다.
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser != null) {
            String nickname = loggedInUser.getNickname();
            String profileImagePath = loggedInUser.getProfileImagePath();

            if (profileImagePath == null || profileImagePath.isEmpty()) {
                profileImagePath = "/default.images/default-profile.jpg";
            }

            model.addAttribute("nickname", nickname);  // 모델에 닉네임 추가
            model.addAttribute("profileImage", profileImagePath); // 프로필 이미지 경로 추가
            model.addAttribute("userRole", loggedInUser.getUserRole().name());
            model.addAttribute("userId", loggedInUser.getId());

            log.info("Logged in user's nickname: " + nickname);
        } else {
            String nickname = "GUEST";
            model.addAttribute("nickname", nickname);  // 모델에 닉네임 추가
            model.addAttribute("profileImage", "/default.images/default-profile.jpg"); // 기본 이미지 추가
            log.info("Guest user logged in.");
        }

        log.info("Welcome to Midea.. ");
    }

}
