package org.astrologist.midea.controller;

import org.astrologist.midea.dto.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/midea")

public class UserPageController {

    // 마이페이지를 보여주는 엔드포인트
    @GetMapping("/mypage")
    public String showMypageForm(Model model) {
        return "userpage/mypage"; // login 뷰 반환
    }
}
