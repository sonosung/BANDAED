package org.astrologist.midea.controller;

import org.astrologist.midea.dto.MyPageUploadDTO;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.service.UserPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/midea")
public class UserPageController {

    @Autowired
    private UserPageService userPageService;  // UserPageService를 주입받아 사용합니다.

    @Autowired
    private HttpSession session;  // 현재 사용자의 세션을 주입받습니다.

    // 마이페이지 폼을 보여주는 GET 요청 처리 메서드입니다.
    @GetMapping("/mypage")
    public String showMypageForm(Model model) {
        // 세션에서 현재 로그인한 사용자 정보를 가져옵니다.
        User loggedInUser = (User) session.getAttribute("user");
        // 사용자가 로그인하지 않은 경우 로그인 페이지로 리다이렉트합니다.
        if (loggedInUser == null) {
            return "redirect:/midea/login?redirectUrl=/midea/mypage";
        }
        // 사용자가 MEMBER 또는 ADMIN 권한이 아닌 경우 로그인 페이지로 리다이렉트합니다.
        if (loggedInUser.getUserRole() != User.UserRole.MEMBER && loggedInUser.getUserRole() != User.UserRole.ADMIN) {
            return "redirect:/midea/login";
        }

        // 사용자의 정보를 DTO로 변환하여 모델에 추가합니다.
        MyPageUploadDTO userPageDTO = MyPageUploadDTO.fromEntity(loggedInUser);
        model.addAttribute("user", userPageDTO);
        return "userpage/mypage";  // 마이페이지 뷰를 반환합니다.
    }

    // 마이페이지 업로드 폼을 보여주는 GET 요청 처리 메서드입니다.
    @GetMapping("/mypage-upload-new")
    public String showMypageUploadForm(Model model) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/midea/login?redirectUrl=/midea/mypage-upload-new";
        }
        if (loggedInUser.getUserRole() != User.UserRole.MEMBER && loggedInUser.getUserRole() != User.UserRole.ADMIN) {
            return "redirect:/midea/login";
        }

        MyPageUploadDTO myPageUploadDTO = MyPageUploadDTO.fromEntity(loggedInUser);
        model.addAttribute("myPageUploadDTO", myPageUploadDTO);
        return "userpage/mypage-upload";  // 새로운 마이페이지 업로드 뷰를 반환합니다.
    }
}
