package org.astrologist.midea.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.astrologist.midea.dto.PageRequestDTO;
import org.astrologist.midea.dto.MindlistDTO;
import org.astrologist.midea.dto.UserDTO;
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


@Controller
@RequestMapping("/midea")
@Log4j2
@RequiredArgsConstructor //자동 주입을 위한 Annotation
public class MindlistController {

    private final MindlistService mindlistService; //MindlistService 인터페이스를 final로 구현.

    @Autowired
    private HttpSession session; // 현재 사용자의 세션을 주입받습니다.

    @GetMapping("/mindlist")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        log.info("list......................" + pageRequestDTO);

        model.addAttribute("result", mindlistService.getList(pageRequestDTO));

    }

    @GetMapping("/mlRegister")
    public void register(Model model){
        // 현재 로그인한 사용자 정보 가져오기
        User loggedInUser = (User) session.getAttribute("user");
        String nickname = loggedInUser.getNickname();
        model.addAttribute("nickname", nickname);  // 모델에 닉네임 추가
        log.info("user role : " + loggedInUser.getUserRole());

//        if (loggedInUser == null) {
//            log.info("로그인 하세요~!");
//            return "redirect:/midea/login";
//        }else {
//            model.addAttribute("nickname", nickname);  // 모델에 닉네임 추가
//            log.info("Logged in user's nickname: " + nickname);
//            return "redirect:/midea/mlRegister";
//        }
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
    public void read(@ModelAttribute("requestDTO") PageRequestDTO requestDTO, Long mno, Model model) {

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