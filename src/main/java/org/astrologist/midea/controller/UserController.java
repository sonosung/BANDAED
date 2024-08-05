package org.astrologist.midea.controller;

import org.astrologist.midea.entity.User;
import org.astrologist.midea.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/midea")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("formType", "signin"); // 기본 폼 유형을 로그인 폼으로 설정
        return "midea/login";
    }

    @PostMapping("/signup")
    public String signUpUser(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("formType", "signup");
            return "midea/login";
        }
        try {
            if (userService.isEmailExist(user.getEmail())) {
                model.addAttribute("signupError", "Email already exists.");
                model.addAttribute("user", user);
                model.addAttribute("formType", "signup");
                return "midea/login";
            }

            if (userService.isNicknameExist(user.getNickname())) {
                model.addAttribute("signupError", "Nickname already exists.");
                model.addAttribute("user", user);
                model.addAttribute("formType", "signup");
                return "midea/login";
            }

            userService.saveUser(user);
            return "redirect:/midea/login";
        } catch (Exception e) {
            model.addAttribute("signupError", "An error occurred: " + e.getMessage());
            model.addAttribute("user", user);
            model.addAttribute("formType", "signup");
            return "midea/login";
        }
    }

    @PostMapping("/signin")
    public String signInUser(@RequestParam String email, @RequestParam String password, Model model) {
        try {
            User user = userService.findByEmail(email);

            if (user == null || !user.getPassword().equals(password)) {
                model.addAttribute("signinError", "Email or password is incorrect.");
                model.addAttribute("user", new User());
                model.addAttribute("formType", "signin");
                return "midea/login";
            }

            return "redirect:/midea/home";
        } catch (Exception e) {
            model.addAttribute("signinError", "An error occurred: " + e.getMessage());
            model.addAttribute("user", new User());
            model.addAttribute("formType", "signin");
            return "midea/login";
        }
    }
}
