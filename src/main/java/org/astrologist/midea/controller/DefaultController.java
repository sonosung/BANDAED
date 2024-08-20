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

    private final HttpSession session;

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

    @GetMapping({"/index", "/contact"})
    public void mindlist(){

        log.info("Welcome to Midea.. ");
    }
}
