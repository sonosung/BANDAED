package org.astrologist.midea.controller;

import org.astrologist.midea.dto.MyPageUploadDTO;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.service.MyPageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/midea")
public class MyPageUploadController {

    @Autowired
    private MyPageUploadService myPageUploadService;

    @Autowired
    private HttpSession session;

    @GetMapping("/mypage-upload")
    public String showMypageUploadForm(Model model) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/midea/login?redirectUrl=/midea/mypage-upload";
        }
        if (loggedInUser.getUserRole() != User.UserRole.MEMBER && loggedInUser.getUserRole() != User.UserRole.ADMIN) {
            return "redirect:/midea/login";
        }

        MyPageUploadDTO myPageUploadDTO = MyPageUploadDTO.fromEntity(loggedInUser);
        model.addAttribute("user", myPageUploadDTO);
        model.addAttribute("myPageUploadDTO", myPageUploadDTO);  // 추가
        return "userpage/mypage-upload";
    }

    @PostMapping("/mypage-upload")
    public String updateMypage(MyPageUploadDTO myPageUploadDTO, MultipartFile profileImage, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/midea/login";
        }

        try {
            myPageUploadService.updateUser(myPageUploadDTO, profileImage, loggedInUser.getId());
            session.setAttribute("user", myPageUploadService.findById(loggedInUser.getId()));
            redirectAttributes.addFlashAttribute("successMessage", "회원정보 수정 완료했습니다!");
            return "redirect:/midea/mypage";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/midea/mypage-upload";
        }
    }
}
