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

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/midea")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    @GetMapping("/login")
    public String showLoginForm(Model model, @RequestParam(value = "redirectUrl", required = false) String redirectUrl) {
        model.addAttribute("user", new UserDTO());
        model.addAttribute("formType", "signin");
        model.addAttribute("redirectUrl", redirectUrl);
        return "userauth/login";
    }

    @PostMapping("/signup")
    public String signUpUser(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return handleFormErrors(model, userDTO, "signup", null);
        }
        try {
            if (userService.isEmailExist(userDTO.getEmail())) {
                return handleFormErrors(model, userDTO, "signup", "이메일이 이미 존재합니다.");
            }

            if (userService.isNicknameExist(userDTO.getNickname())) {
                return handleFormErrors(model, userDTO, "signup", "닉네임이 이미 존재합니다.");
            }

            User user = userDTO.toEntity();
            userService.saveUser(user);
            return "redirect:/midea/login";
        } catch (Exception e) {
            return handleFormErrors(model, userDTO, "signup", "An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/signin")
    public String signInUser(@RequestParam String email, @RequestParam String password, @RequestParam(required = false) String redirectUrl, Model model) {
        try {
            User user = userService.findByEmail(email);
            if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
                return handleFormErrors(model, new UserDTO(), "signin", "이메일 혹은 패스워드가 일치하지 않습니다.");
            }

            session.setAttribute("user", user);
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                return "redirect:" + redirectUrl;
            }
            return "redirect:/midea/index";
        } catch (Exception e) {
            return handleFormErrors(model, new UserDTO(), "signin", "An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/cancel")
    public String cancelMembership(@RequestParam Long userId) {
        userService.deactivateUser(userId);
        session.invalidate();
        return "redirect:/midea/login";
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
        return "userauth/login";
    }
}
