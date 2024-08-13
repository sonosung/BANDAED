package org.astrologist.midea.controller;

import org.astrologist.midea.dto.MyPageUploadDTO;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.service.MyPageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    private MyPageUploadService myPageUploadService; // MyPageUploadService를 주입받아 사용합니다.

    @Autowired
    private HttpSession session;  // 현재 세션을 통해 로그인된 사용자의 정보를 관리합니다.

    @GetMapping("/mypage-upload")
    public String showMypageUploadForm(Model model, RedirectAttributes redirectAttributes) {
        User loggedInUser = getLoggedInUser(); // 현재 로그인된 사용자 정보를 가져옵니다.
        if (loggedInUser == null) { // 사용자가 로그인되지 않은 경우
            return "redirect:/midea/login?redirectUrl=/midea/mypage-upload";
        }

        if (!isAuthorizedUser(loggedInUser)) { // 사용자가 적절한 권한을 가지고 있지 않은 경우
            redirectAttributes.addFlashAttribute("errorMessage", "다시 회원가입 해주세요.");
            return "redirect:/midea/login";
        }

        MyPageUploadDTO myPageUploadDTO = MyPageUploadDTO.fromEntity(loggedInUser); // 사용자 정보를 DTO로 변환합니다.
        model.addAttribute("user", myPageUploadDTO); // 사용자 정보를 모델에 추가합니다.
        model.addAttribute("myPageUploadDTO", myPageUploadDTO); // 모델에 DTO를 추가합니다.
        return "userpage/mypage-upload"; // 마이페이지 업로드 폼 뷰를 반환합니다.
    }

    @PostMapping("/mypage-upload")
    public String updateMypage(MyPageUploadDTO myPageUploadDTO, MultipartFile profileImage, RedirectAttributes redirectAttributes) {
        User loggedInUser = getLoggedInUser();  // 현재 로그인된 사용자 정보를 가져옵니다.
        if (loggedInUser == null) { // 사용자가 로그인되지 않은 경우
            return "redirect:/midea/login";
        }

        try {
            myPageUploadService.updateUser(myPageUploadDTO, profileImage, loggedInUser.getId());  // 사용자 정보를 업데이트합니다.
            session.setAttribute("user", myPageUploadService.findById(loggedInUser.getId()));  // 업데이트된 사용자 정보를 세션에 다시 저장합니다.
            redirectAttributes.addFlashAttribute("successMessage", "회원정보 수정 완료했습니다!");
            return "redirect:/midea/mypage";  // 마이페이지로 리다이렉트합니다.
        } catch (IllegalArgumentException e) {  // IllegalArgumentException 발생 시 처리
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage()); // 오류 메시지를 플래시 속성으로 전달합니다.
            return "redirect:/midea/mypage-upload"; // 다시 마이페이지 업로드 폼으로 리다이렉트합니다.
        } catch (Exception e) {  // 그 외의 예외 발생 시 처리
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "회원정보 수정 중 오류가 발생했습니다.");
            return "redirect:/midea/mypage-upload";
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage()); // 예외 메시지를 클라이언트에게 반환합니다.
    }

    private User getLoggedInUser() {
        return (User) session.getAttribute("user");
    }
    /**
     * 사용자가 적절한 권한을 가지고 있는지 확인하는 메서드입니다.
     *
     * @param user 검사할 사용자 객체
     * @return 사용자가 MEMBER 또는 ADMIN 권한을 가지고 있으면 true, 그렇지 않으면 false
     */
    private boolean isAuthorizedUser(User user) {
        return user.getUserRole() == User.UserRole.MEMBER || user.getUserRole() == User.UserRole.ADMIN;
    }
}
