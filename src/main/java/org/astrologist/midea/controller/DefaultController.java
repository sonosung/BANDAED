package org.astrologist.midea.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.astrologist.midea.dto.UserDTO;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.repository.MyPageUploadRepository;
import org.astrologist.midea.service.MindlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/midea")
@Log4j2
@RequiredArgsConstructor
public class DefaultController {

    private final MindlistService mindlistService; //MindlistService 인터페이스를 final로 구현.

    @Autowired
    private final HttpSession session;

    @Autowired
    private MyPageUploadRepository myPageUploadRepository;

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

    @GetMapping({"/portfolio"})
    public void midea(){

        log.info("portfolio......................");
    }

    @GetMapping({"/index", "/contact"})
    public void mindlist(Model model, User user, Long userId, UserDTO userDTO) {

        // 세션에서 현재 로그인한 사용자 정보를 가져옵니다.
        User loggedInUser = (User) session.getAttribute("user");


        if (loggedInUser != null) {
            String nickname = loggedInUser.getNickname();
//            String profileImage = loggedInUser.getProfileImagePath();
            model.addAttribute("nickname", nickname);  // 모델에 닉네임 추가

            log.info("Logged in user's nickname: " + nickname);

        } else {
            String nickname = "GUEST";
            model.addAttribute("nickname", nickname);  // 모델에 닉네임 추가
        }

        model.addAttribute("userRole", user.getUserRole().name());
        model.addAttribute("userId", user.getId());

/*        String profileImagePath = user.getProfileImagePath();

        if (profileImagePath == null || profileImagePath.isEmpty()) {
            profileImagePath = "/default.images/default-profile.jpg";
        } else {
            profileImagePath = userImage;
        }
        model.addAttribute("profileImage", profileImagePath);*/

        String profileImagePath = user.getProfileImagePath();

        if (profileImagePath == null || profileImagePath.isEmpty()) {
            profileImagePath = "/default.images/default-profile.jpg";
            model.addAttribute("profileImage", profileImagePath);
        }
        else {
//            String profileImage = loggedInUser.getProfileImagePath();
            model.addAttribute("profileImage", profileImagePath);
        }

        log.info("Welcome to Midea.. ");
    }

}
