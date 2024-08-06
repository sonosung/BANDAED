//package org.astrologist.midea.controller;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.astrologist.midea.dto.PageRequestDTO;
//import org.astrologist.midea.dto.CommunityDTO;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//
//@Controller
//@RequestMapping("/wooxtravel")
//@Log4j2
//@RequiredArgsConstructor //자동 주입을 위한 Annotation
//public class CommunityController {
//
//    @GetMapping({"/home", "/community", "/contact", "/about", "/login", "/index", "/reservation", "/deals"})
//    public void midea(){
//
//        log.info("midea......................");
//    }
//
//    private final CommunityService service; //GuestBookService 인터페이스를 final로 구현.
//
//    @GetMapping("/")
//    public String index() {
//        return "redirect:/midea/index";
//
//    }
//
//    @GetMapping("/community")
//    public void list(PageRequestDTO pageRequestDTO, Model model){
//
//        log.info("list......................" + pageRequestDTO);
//
//        model.addAttribute("result", service.getList(pageRequestDTO));
//
//    }
//
//    @GetMapping("/register")
//    public void register(){
//        log.info("register get...");
//    }
//
//    @PostMapping("/register")
//    public String registerPost(CommunityDTO dto, RedirectAttributes redirectAttributes){
//        log.info("dto....." + dto);
//
//        //새로 추가된 엔티티의 번호
//        Long gno = service.register(dto);
//
//        redirectAttributes.addFlashAttribute("msg", gno);
//
//        return "redirect:/wooxtravel/community";
//    }
//
//    //    @GetMapping("/read")
//    @GetMapping({"/read", "/modify"}) //수정과 삭제 모두 read()가 필요하므로, 한번에 맵핑ㄹ
//    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {
//        log.info("gno: " + gno);
//
//        CommunityDTO dto = service.read(gno);
//
//        model.addAttribute("dto", dto);
//    }
//
//    @PostMapping("/remove")
//    public String remove(long gno, RedirectAttributes redirectAttributes){
//
//        log.info("gno: " + gno);
//
//        service.remove(gno);
//
//        redirectAttributes.addFlashAttribute("msg", gno);
//
//        return "redirect:/wooxtravel/community";
//    }
//
//    @PostMapping("/modify")
//    public String modify(CommunityDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes) {
//
//        log.info("post modify................................................");
//        log.info("dto: " + dto);
//
//        service.modify(dto);
//
//        redirectAttributes.addAttribute("page", requestDTO.getPage());
//        redirectAttributes.addAttribute("type", requestDTO.getType());
//        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
//        redirectAttributes.addAttribute("gno", dto.getGno());
//
//        return "redirect:/wooxtravel/read";
//    }
//
//}