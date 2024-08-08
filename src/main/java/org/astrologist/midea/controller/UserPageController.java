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
    private UserPageService userPageService;

    @Autowired
    private HttpSession session;

    @GetMapping("/mypage")
    public String showMypageForm(Model model) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/midea/login?redirectUrl=/midea/mypage";
        }
        if (loggedInUser.getUserRole() != User.UserRole.MEMBER && loggedInUser.getUserRole() != User.UserRole.ADMIN) {
            return "redirect:/midea/login";
        }

        MyPageUploadDTO userPageDTO = MyPageUploadDTO.fromEntity(loggedInUser);
        model.addAttribute("user", userPageDTO);
        return "userpage/mypage";
    }

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
        return "userpage/mypage-upload";
    }
}
