package org.astrologist.midea.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.astrologist.midea.dto.MindlistAdminDTO;
import org.astrologist.midea.dto.MyPageUploadDTO;
import org.astrologist.midea.dto.PageRequestDTO;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.service.MindlistAdminService;
import org.astrologist.midea.service.UserPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/midea")
@Log4j2
@RequiredArgsConstructor //자동 주입을 위한 Annotation
public class MindlistAdminController {

    private final MindlistAdminService mindlistAdminService; //MindlistService 인터페이스를 final로 구현.

    @Autowired
    private UserPageService userPageService;  // UserPageService를 주입받아 사용합니다.

    @Autowired
    private HttpSession session;  // 현재 사용자의 세션을 주입받습니다.
    
    @GetMapping("/mindlistAdmin")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        log.info("list......................" + pageRequestDTO);

        model.addAttribute("result", mindlistAdminService.getList(pageRequestDTO));

    }

    @GetMapping("/mlAdminRegister")
    public void register(Model model){

        // 세션에서 현재 로그인한 사용자 정보를 가져옵니다.
        User loggedInUser = (User) session.getAttribute("user");

        // 사용자의 정보를 DTO로 변환하여 모델에 추가합니다.
        MyPageUploadDTO userPageDTO = MyPageUploadDTO.fromEntity(loggedInUser);
        model.addAttribute("user", userPageDTO);

        log.info("user name : " + loggedInUser);

    }

    @PostMapping("/mlAdminRegister")
    public String register(MindlistAdminDTO mindlistAdminDTO, RedirectAttributes redirectAttributes, Model model){
        log.info("dto....." + mindlistAdminDTO);

        //새로 추가된 엔티티의 번호
        Long mno = mindlistAdminService.register(mindlistAdminDTO);

        redirectAttributes.addFlashAttribute("msg", mno);

        // 세션에서 현재 로그인한 사용자 정보를 가져옵니다.
        User loggedInUser = (User) session.getAttribute("user");

        // 사용자의 정보를 DTO로 변환하여 모델에 추가합니다.
        MyPageUploadDTO userPageDTO = MyPageUploadDTO.fromEntity(loggedInUser);
        model.addAttribute("user", userPageDTO);

        if (loggedInUser == null) {
            return "redirect:/midea/login";
        }

        return "redirect:/midea/mindlistAdmin";
    }

    @GetMapping({"/mlAdminread", "/mlAdminmodify"}) //수정과 삭제 모두 read()가 필요하므로, 한번에 맵핑
    public void read(long mno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {
        log.info("mno: " + mno);

        MindlistAdminDTO mindlistAdminDTO = mindlistAdminService.read(mno);

        model.addAttribute("dto", mindlistAdminDTO);
    }

    @PostMapping("/mlAdminremove")
    public String remove(long mno, RedirectAttributes redirectAttributes){

        log.info("mno: " + mno);

        mindlistAdminService.remove(mno);

        redirectAttributes.addFlashAttribute("msg", mno);

        return "redirect:/midea/mindlistAdmin";
    }

    @PostMapping("/mlAdminmodify")
    public String modify(MindlistAdminDTO mindlistAdminDTO, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes) {

        log.info("post modify................................................");
        log.info("dto: " + mindlistAdminDTO);

        mindlistAdminService.modify(mindlistAdminDTO);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("mno", mindlistAdminDTO.getMno());

        return "redirect:/midea/mlAdminread";
    }

}