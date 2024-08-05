package org.astrologist.midea.controller;

import org.astrologist.midea.entity.User;
import org.astrologist.midea.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/midea")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User()); // Model에 'user' 객체 추가
        return "midea/login";
    }

    @PostMapping("/signup")
    public String signUpUser(@ModelAttribute User user, Model model) {
        if (userService.isEmailExist(user.getEmail())) {
            model.addAttribute("error", "Email already exists.");
            model.addAttribute("user", user); // Model에 'user' 객체 추가
            return "midea/login";
        }

        if (userService.isNicknameExist(user.getNickname())) {
            model.addAttribute("error", "Nickname already exists.");
            model.addAttribute("user", user); // Model에 'user' 객체 추가
            return "midea/login";
        }

        userService.saveUser(user);
        return "redirect:/midea/login";
    }

    @PostMapping("/signin")
    public String signInUser(@RequestParam String email, @RequestParam String password, Model model) {
        User user = userService.findByEmail(email);

        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("error", "email 혹은 password가 일치하지 않습니다.");
            model.addAttribute("user", new User()); // Model에 'user' 객체 추가
            return "midea/login";
        }

        // 로그인 성공 후 처리 로직
        return "redirect:/midea/home";
    }
}
