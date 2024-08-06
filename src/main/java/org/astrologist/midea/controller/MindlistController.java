package org.astrologist.midea.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.astrologist.midea.dto.PageRequestDTO;
import org.astrologist.midea.dto.MindlistDTO;
import org.astrologist.midea.service.MindlistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/wooxtravel")
@Log4j2
@RequiredArgsConstructor //자동 주입을 위한 Annotation
public class MindlistController {

    @GetMapping({"/index", "/community", "/contact", "/about"})
    public void mindlist(){

        log.info("mindlist......................");
    }

    private final MindlistService service; //MindlistService 인터페이스를 final로 구현.

    @GetMapping("/")
    public String index() {
        return "redirect:/wooxtravel/index";

    }

    @GetMapping("/mindlist")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        log.info("list......................" + pageRequestDTO);

        model.addAttribute("result", service.getList(pageRequestDTO));

    }

    @GetMapping("/register")
    public void register(){
        log.info("register get...");
    }

    @PostMapping("/register")
    public String registerPost(MindlistDTO dto, RedirectAttributes redirectAttributes){
        log.info("dto....." + dto);

        //새로 추가된 엔티티의 번호
        Long mno = service.register(dto);

        redirectAttributes.addFlashAttribute("msg", mno);

        return "redirect:/wooxtravel/mindlist";
    }

    //    @GetMapping("/read")
    @GetMapping({"/read", "/modify"}) //수정과 삭제 모두 read()가 필요하므로, 한번에 맵핑ㄹ
    public void read(long mno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {
        log.info("mno: " + mno);

        MindlistDTO dto = service.read(mno);

        model.addAttribute("dto", dto);
    }

    @PostMapping("/remove")
    public String remove(long mno, RedirectAttributes redirectAttributes){

        log.info("mno: " + mno);

        service.remove(mno);

        redirectAttributes.addFlashAttribute("msg", mno);

        return "redirect:/wooxtravel/mindlist";
    }

    @PostMapping("/modify")
    public String modify(MindlistDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes) {

        log.info("post modify................................................");
        log.info("dto: " + dto);

        service.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("mno", dto.getMno());

        return "redirect:/wooxtravel/read";
    }

}