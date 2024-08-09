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
    private MyPageUploadService myPageUploadService;  // MyPageUploadService를 주입받아 사용합니다.

    @Autowired
    private HttpSession session;  // 현재 사용자의 세션을 주입받습니다.

    // 마이페이지 업로드 폼을 보여주는 GET 요청 처리 메서드입니다.
    @GetMapping("/mypage-upload")
    public String showMypageUploadForm(Model model) {
        // 세션에서 현재 로그인한 사용자 정보를 가져옵니다.
        User loggedInUser = (User) session.getAttribute("user");
        // 사용자가 로그인하지 않은 경우 로그인 페이지로 리다이렉트합니다.
        if (loggedInUser == null) {
            return "redirect:/midea/login?redirectUrl=/midea/mypage-upload";
        }
        // 사용자가 MEMBER 또는 ADMIN 권한이 아닌 경우 로그인 페이지로 리다이렉트합니다.
        if (loggedInUser.getUserRole() != User.UserRole.MEMBER && loggedInUser.getUserRole() != User.UserRole.ADMIN) {
            return "redirect:/midea/login";
        }

        // 사용자의 정보를 DTO로 변환하여 모델에 추가합니다.
        MyPageUploadDTO myPageUploadDTO = MyPageUploadDTO.fromEntity(loggedInUser);
        model.addAttribute("user", myPageUploadDTO);
        model.addAttribute("myPageUploadDTO", myPageUploadDTO);  // 추가로 DTO를 모델에 저장합니다.
        return "userpage/mypage-upload";  // 마이페이지 업로드 뷰를 반환합니다.
    }

    // 마이페이지 정보와 프로필 이미지를 업데이트하는 POST 요청 처리 메서드입니다.
    @PostMapping("/mypage-upload")
    public String updateMypage(MyPageUploadDTO myPageUploadDTO, MultipartFile profileImage, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("user");
        // 사용자가 로그인하지 않은 경우 로그인 페이지로 리다이렉트합니다.
        if (loggedInUser == null) {
            return "redirect:/midea/login";
        }

        try {
            // 서비스 클래스를 통해 사용자의 정보를 업데이트합니다.
            myPageUploadService.updateUser(myPageUploadDTO, profileImage, loggedInUser.getId());
            // 업데이트된 사용자 정보를 세션에 다시 저장합니다.
            session.setAttribute("user", myPageUploadService.findById(loggedInUser.getId()));
            redirectAttributes.addFlashAttribute("successMessage", "회원정보 수정 완료했습니다!");
            return "redirect:/midea/mypage";  // 업데이트 완료 후 마이페이지로 리다이렉트합니다.
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/midea/mypage-upload";  // 오류 발생 시 업로드 페이지로 리다이렉트합니다.
        }
    }
}
