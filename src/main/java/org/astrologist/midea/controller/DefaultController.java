package org.astrologist.midea.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.service.MindlistService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/midea")
@Log4j2
@RequiredArgsConstructor
public class DefaultController {

    private final MindlistService mindlistService; //MindlistService 인터페이스를 final로 구현.

    @GetMapping("/")
    public String index() {
        return "redirect:/midea/index";

    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        log.info("Logout......................");

        return "redirect:/midea/index";
    }

    @GetMapping({"/portfolio"})
    public void midea(){

        log.info("portfolio......................");
    }

    @GetMapping({"/index"/*, "/community"*/, "/contact"})
    public void mindlist(HttpSession session){

        // 세션에서 현재 로그인한 사용자 정보를 가져옵니다.
        User loggedInUser = (User) session.getAttribute("user");

        log.info("Welcome to Midea.. " + loggedInUser);
    }
}
