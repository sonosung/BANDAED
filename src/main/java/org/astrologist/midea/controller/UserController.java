package org.astrologist.midea.controller;

import org.astrologist.midea.dto.UserDTO;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.service.UserService;
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

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new UserDTO()); // 새로운 사용자 DTO 객체를 모델에 추가
        model.addAttribute("formType", "signin"); // 기본 폼 유형을 로그인 폼으로 설정
        return "midea/login"; // login 뷰 반환
    }

    @PostMapping("/signup")
    public String signUpUser(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return handleFormErrors(model, userDTO, "signup", null);
        }
        try {
            if (userService.isEmailExist(userDTO.getEmail())) {
                return handleFormErrors(model, userDTO, "signup", "Email already exists.");
            }

            if (userService.isNicknameExist(userDTO.getNickname())) {
                return handleFormErrors(model, userDTO, "signup", "Nickname already exists.");
            }

            // UserDTO를 User 엔티티로 변환하여 저장
            User user = new User();
            user.setNickname(userDTO.getNickname());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            userService.saveUser(user);
            return "redirect:/midea/login";
        } catch (Exception e) {
            return handleFormErrors(model, userDTO, "signup", "An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/signin")
    public String signInUser(@RequestParam String email, @RequestParam String password, Model model) {
        try {
            User user = userService.findByEmail(email);

            if (user == null || !user.getPassword().equals(password)) {
                return handleFormErrors(model, new UserDTO(), "signin", "이메일 혹은 패스워드가 일치하지 않습니다.");
            }

            return "redirect:/midea/home"; // 홈 페이지로 리다이렉트
        } catch (Exception e) {
            return handleFormErrors(model, new UserDTO(), "signin", "An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/check-nickname")
    @ResponseBody
    public boolean checkNickname(@RequestParam String nickname) {
        return userService.isNicknameExist(nickname);
    }

    @GetMapping("/check-email")
    @ResponseBody
    public boolean checkEmail(@RequestParam String email) {
        return userService.isEmailExist(email);
    }

    private String handleFormErrors(Model model, UserDTO userDTO, String formType, String errorMessage) {
        model.addAttribute("user", userDTO);
        model.addAttribute("formType", formType);
        if ("signup".equals(formType) && errorMessage != null) {
            model.addAttribute("signupError", errorMessage);
        } else if ("signin".equals(formType) && errorMessage != null) {
            model.addAttribute("signinError", errorMessage);
        }
        return "midea/login";
    }
}
