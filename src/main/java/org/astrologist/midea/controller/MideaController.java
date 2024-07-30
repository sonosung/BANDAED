package org.astrologist.midea.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/midea")
@Log4j2
@RequiredArgsConstructor //자동 주입을 위한 Annotation
public class MideaController {

    @GetMapping({"/home", "/login", "/contact", "/about", "/index"})
    public void midea(){
        log.info("midea......................");
    }

}
