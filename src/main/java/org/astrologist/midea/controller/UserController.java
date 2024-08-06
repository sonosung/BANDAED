package org.astrologist.midea.controller;

import org.astrologist.midea.dto.UserDTO;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.service.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/midea")
public class UserController {

    @Autowired
    private UserService userService;

    // 로그인 폼을 보여주는 엔드포인트
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new UserDTO()); // 새로운 사용자 DTO 객체를 모델에 추가
        model.addAttribute("formType", "signin"); // 기본 폼 유형을 로그인 폼으로 설정
        return "midea/login"; // login 뷰 반환
    }

    // 회원가입 폼 제출을 처리하는 엔드포인트
    @PostMapping("/signup")
    public String signUpUser(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // 폼 검증 에러가 있는 경우 처리
            return handleFormErrors(model, userDTO, "signup", null);
        }
        try {
            if (userService.isEmailExist(userDTO.getEmail())) {
                // 이메일이 이미 존재하는 경우 처리
                return handleFormErrors(model, userDTO, "signup", "이메일이 이미 존재합니다.");
            }

            if (userService.isNicknameExist(userDTO.getNickname())) {
                // 닉네임이 이미 존재하는 경우 처리
                return handleFormErrors(model, userDTO, "signup", "닉네임이 이미 존재합니다.");
            }

            // UserDTO를 User 엔티티로 변환하여 저장
            User user = userDTO.toEntity(); // toEntity 메서드를 사용하여 DTO를 엔티티로 변환
            userService.saveUser(user); // 사용자 정보 저장
            return "redirect:/midea/login"; // 로그인 페이지로 리다이렉트
        } catch (Exception e) {
            // 기타 예외 처리
            return handleFormErrors(model, userDTO, "signup", "An error occurred: " + e.getMessage());
        }
    }

    // 로그인 폼 제출을 처리하는 엔드포인트
    @PostMapping("/signin")
    public String signInUser(@RequestParam String email, @RequestParam String password, Model model) {
        try {
            User user = userService.findByEmail(email); // 이메일로 사용자 찾기

            if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
                // 사용자 정보가 일치하지 않는 경우 처리
                return handleFormErrors(model, new UserDTO(), "signin", "이메일 혹은 패스워드가 일치하지 않습니다.");
            }

            return "redirect:/midea/home"; // 홈 페이지로 리다이렉트
        } catch (Exception e) {
            // 기타 예외 처리
            return handleFormErrors(model, new UserDTO(), "signin", "An error occurred: " + e.getMessage());
        }
    }

    // 닉네임 중복 체크 엔드포인트
    @GetMapping("/check-nickname")
    @ResponseBody
    public boolean checkNickname(@RequestParam String nickname) {
        return userService.isNicknameExist(nickname); // 닉네임 존재 여부 반환
    }

    // 이메일 중복 체크 엔드포인트
    @GetMapping("/check-email")
    @ResponseBody
    public boolean checkEmail(@RequestParam String email) {
        return userService.isEmailExist(email); // 이메일 존재 여부 반환
    }

    // 폼 검증 에러를 처리하는 메서드
    private String handleFormErrors(Model model, UserDTO userDTO, String formType, String errorMessage) {
        model.addAttribute("user", userDTO); // 모델에 사용자 DTO 추가
        model.addAttribute("formType", formType); // 폼 유형 설정
        if ("signup".equals(formType) && errorMessage != null) {
            model.addAttribute("signupError", errorMessage); // 회원가입 에러 메시지 설정
        } else if ("signin".equals(formType) && errorMessage != null) {
            model.addAttribute("signinError", errorMessage); // 로그인 에러 메시지 설정
        }
        return "midea/login"; // 로그인 뷰 반환
    }
}
