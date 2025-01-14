package org.astrologist.midea.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.astrologist.midea.dto.UserDTO;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.service.UserService;
import org.astrologist.midea.service.SpotifyUserService;  // SpotifyUserService 임포트 추가
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.io.IOException;

@Controller
@RequestMapping("/midea")  // 이 컨트롤러의 기본 경로는 "/midea"
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SpotifyUserService spotifyService;  // SpotifyUserService 주입

    @Autowired
    private HttpSession session;

    @GetMapping("/login")
    public String showLoginForm(Model model, @RequestParam(value = "redirectUrl", required = false) String redirectUrl) {
        model.addAttribute("user", new UserDTO());
        model.addAttribute("formType", "signin");
        model.addAttribute("redirectUrl", redirectUrl);
        return "userauth/login";
    }

    @GetMapping("/spotify/login")
    public void redirectToSpotifyLogin(HttpServletResponse response) throws IOException {
        String spotifyLoginUrl = spotifyService.getSpotifyLoginUrl();
        response.sendRedirect(spotifyLoginUrl);
    }

    @GetMapping("/spotify/callback")
    public String handleSpotifyCallback(@RequestParam("code") String code, Model model) {
        try {
            User user = spotifyService.handleSpotifyCallback(code);  // SpotifyUserService 사용
            session.setAttribute("user", user);
            return "redirect:/midea/index";
        } catch (Exception e) {
            return handleFormErrors(model, "signin", "Spotify 로그인 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 기존 회원가입 메서드
    @PostMapping("/signup")
    public String signUpUser(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult, Model model) {
        // 입력 값 검증 중 오류가 발생한 경우
        if (bindingResult.hasErrors()) {
            return handleFormErrors(model, userDTO, "signup", null);  // 오류 처리 후 다시 회원가입 폼으로 이동
        }
        try {
            // 이메일이 이미 존재하는지 확인
            if (userService.isEmailExist(userDTO.getEmail())) {
                return handleFormErrors(model, userDTO, "signup", "이메일이 이미 존재합니다.");
            }

            // 닉네임이 이미 존재하는지 확인
            if (userService.isNicknameExist(userDTO.getNickname())) {
                return handleFormErrors(model, userDTO, "signup", "닉네임이 이미 존재합니다.");
            }

            // UserDTO를 User 엔티티로 변환하여 저장
            User user = userDTO.toEntity();
            userService.saveUser(user);
            model.addAttribute("signupSuccess", true);  // 회원가입 성공 메시지 추가
            return "userauth/login";  // 로그인 폼으로 이동
        } catch (Exception e) {
            return handleFormErrors(model, userDTO, "signup", "An error occurred: " + e.getMessage());  // 예외 발생 시 오류 처리
        }
    }

    @PostMapping("/signin")
    public String signInUser(@RequestParam String email, @RequestParam String password, @RequestParam(required = false) String redirectUrl, Model model) {
        // 이메일과 패스워드 유효성 검사
        if (email == null || email.trim().isEmpty()) {
            return handleFormErrors(model, new UserDTO(), "signin", "이메일을 입력해주세요.");
        }
        if (password == null || password.trim().isEmpty()) {
            return handleFormErrors(model, new UserDTO(), "signin", "패스워드를 입력해주세요.");
        }

        try {
            User user = userService.findByEmail(email);
            if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
                return handleFormErrors(model, new UserDTO(), "signin", "이메일 혹은 패스워드가 일치하지 않습니다.");
            }

            // UserRole이 GUEST라면 세션 무효화 후 로그인 폼으로 리디렉션
            if (user.getUserRole() == User.UserRole.GUEST) {
                session.invalidate();  // 세션 무효화
                return handleFormErrors(model, new UserDTO(), "signin", "탈퇴한 회원입니다. 다시 가입해주세요");
            }

            session.setAttribute("user", user);  // 세션에 사용자 정보 저장
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                return "redirect:" + redirectUrl;
            }
            return "redirect:/midea/index";  // 기본 페이지로 리디렉션
        } catch (Exception e) {
            return handleFormErrors(model, new UserDTO(), "signin", "오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/cancel")
    public String cancelMembership(@RequestParam Long userId) {
        // 사용자를 비활성화 처리
        userService.deactivateUser(userId);
        session.invalidate();  // 세션 무효화
        return "redirect:/midea/login";  // 로그인 페이지로 리디렉션
    }

    @GetMapping("/check-nickname")
    @ResponseBody
    public boolean checkNickname(@RequestParam String nickname) {
        // 닉네임 존재 여부 확인 (비동기 응답)
        return userService.isNicknameExist(nickname);
    }

    @GetMapping("/check-email")
    @ResponseBody
    public boolean checkEmail(@RequestParam String email) {
        // 이메일 존재 여부 확인 (비동기 응답)
        return userService.isEmailExist(email);
    }

    // formType과 errorMessage만 사용하는 handleFormErrors 메서드 추가
    private String handleFormErrors(Model model, String formType, String errorMessage) {
        model.addAttribute("formType", formType);
        model.addAttribute("errorMessage", errorMessage);
        return "userauth/login";  // 로그인 폼 뷰로 이동
    }

    // 기존 handleFormErrors 메서드와 중복 사용 가능
    private String handleFormErrors(Model model, UserDTO userDTO, String formType, String errorMessage) {
        model.addAttribute("user", userDTO);  // 모델에 UserDTO 객체 추가
        model.addAttribute("formType", formType);  // 폼 타입을 설정 (로그인 또는 회원가입)
        // 폼 타입에 따라 적절한 오류 메시지 추가
        if ("signup".equals(formType) && errorMessage != null) {
            model.addAttribute("signupError", errorMessage);
        } else if ("signin".equals(formType) && errorMessage != null) {
            model.addAttribute("signinError", errorMessage);
        }
        return "userauth/login";  // 로그인 폼 뷰로 이동
    }

    @PostMapping("/deactivate")
    public String deactivateUser() {
        User loggedInUser = (User) session.getAttribute("user");  // 세션에서 로그인된 사용자 정보 가져오기
        if (loggedInUser != null) {
            userService.deactivateUser(loggedInUser.getId());  // 사용자 비활성화
            session.invalidate();  // 세션 무효화
        }
        return "redirect:/midea/login";  // 로그인 페이지로 리디렉션
    }

}
