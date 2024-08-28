package org.astrologist.midea.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.astrologist.midea.dto.*;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.service.MindlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;


@Controller
@RequestMapping("/midea")
@Log4j2
@RequiredArgsConstructor //자동 주입을 위한 Annotation
public class MindlistController {

    private final MindlistService mindlistService; //MindlistService 인터페이스를 final로 구현.

    @Autowired
    private HttpSession session; // 현재 사용자의 세션을 주입받습니다.

    @GetMapping("/mindlist")
    public void list(PageRequestDTO pageRequestDTO, Model model, AlgorithmRequestDTO algorithmRequestDTO, User user, UserDTO userDTO){

        String userImage = userDTO.getProfileImagePath();

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser != null) {
            String nickname = loggedInUser.getNickname();
            model.addAttribute("nickname", nickname);  // 모델에 닉네임 추가
            log.info("Logged in user's nickname: " + nickname);
        } else {
            String nickname = "GUEST";
            model.addAttribute("nickname", nickname);  // 모델에 닉네임 추가
        }

        log.info("list......................" + pageRequestDTO);

        log.info("algorithm......................" + algorithmRequestDTO);

        model.addAttribute("result", mindlistService.getList(pageRequestDTO));

        model.addAttribute("algorithm", mindlistService.getAlgorithmList(algorithmRequestDTO));

        String profileImagePath = user.getProfileImagePath();

        if (profileImagePath == null || profileImagePath.isEmpty()) {
            profileImagePath = "/default.images/default-profile.jpg";
        } else {
            profileImagePath = userImage;
        }
        model.addAttribute("profileImage", profileImagePath);

    }

    @GetMapping("/mlRegister")
    public String register(Model model) {

        // 현재 로그인한 사용자 정보 가져와서, 로그인 하지 않았으면 로그인 페이지로 이동시킴.
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            log.info("Please Login~!");
            return "redirect:/midea/login";
        } else {
            String nickname = loggedInUser.getNickname();
            model.addAttribute("nickname", nickname);  // 모델에 닉네임 추가
            log.info("Logged in user's nickname: " + nickname);
            log.info("user role : " + loggedInUser.getUserRole());
        }
        return "midea/mlRegister";
    }

    @PostMapping("/mlRegister")
    public String register(MindlistDTO dto, RedirectAttributes redirectAttributes, User user){

        log.info("dto....." + dto);

        //새로 추가된 엔티티의 번호
        Long mno = mindlistService.register(dto);

        redirectAttributes.addFlashAttribute("msg", mno);

        return "redirect:/midea/mindlist";
    }

    @GetMapping({"/mlread", "/mlmodify"}) //수정과 삭제 모두 read()가 필요하므로, 한번에 맵핑
    public void read(@ModelAttribute("requestDTO") PageRequestDTO requestDTO, Long mno, Model model, AlgorithmRequestDTO algorithmRequestDTO) {

        log.info("mno: " + mno);

        MindlistDTO mindlistDTO = mindlistService.read(mno);

        log.info(mindlistDTO);

        model.addAttribute("dto", mindlistDTO);
    }

    @PostMapping("/mlremove")
    public String remove(long mno, RedirectAttributes redirectAttributes){

        log.info("mno: " + mno);

        mindlistService.removeWithComments(mno);

        redirectAttributes.addFlashAttribute("msg", mno);

        return "redirect:/midea/mindlist";
    }

    @PostMapping("/mlmodify")
    public String modify(MindlistDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes) {

        log.info("post modify....");
        log.info("dto: " + dto);

        mindlistService.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("mno", dto.getMno());

        return "redirect:/midea/mlread";
    }

}