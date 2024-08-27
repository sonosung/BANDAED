package org.astrologist.midea.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.astrologist.midea.dto.AlgorithmRequestDTO;
import org.astrologist.midea.dto.MindlistAdminDTO;
import org.astrologist.midea.dto.PageRequestDTO;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.service.MindlistAdminService;
import org.astrologist.midea.service.UserPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.astrologist.midea.entity.User.UserRole.*;

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
    public void list(PageRequestDTO pageRequestDTO, Model model, AlgorithmRequestDTO algorithmRequestDTO, User user){

        String userImage = user.getProfileImagePath();

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

        model.addAttribute("result", mindlistAdminService.getList(pageRequestDTO));

        model.addAttribute("algorithm", mindlistAdminService.getAlgorithmList(algorithmRequestDTO));

        String profileImagePath = user.getProfileImagePath();

        if (profileImagePath == null || profileImagePath.isEmpty()) {
            profileImagePath = "/default.images/default-profile.jpg";
        } else {
            profileImagePath = userImage;
        }
        model.addAttribute("profileImage", profileImagePath);

    }

    @GetMapping("/mlAdminRegister")
    public String register(Model model, RedirectAttributes redirectAttributes){

//        // 세션에서 현재 로그인한 사용자 정보를 가져옵니다.
//        User user = (User) session.getAttribute("user");
//
//        // 사용자가 로그인하지 않은 경우 로그인 페이지로 리다이렉트합니다.
//        if (user == null) {
//            return "redirect:/midea/login";
//        }
//        // 사용자가 ADMIN 권한이 아닌 경우 이전 페이지로 리다이렉트합니다.
//        if (user.getUserRole() != ADMIN) {
//            return "redirect:/midea/mindlistAdmin";
//        }
//
//        log.info("loggedInUser Post....." + user);
//
//        return "redirect:/midea/mindlistAdmin";

        // 현재 로그인한 사용자 정보 가져오기
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            log.info("로그인 하세요~!");
            return "redirect:/midea/login";
        }
        if (loggedInUser != null && loggedInUser.getUserRole() == ADMIN) {
            String nickname = loggedInUser.getNickname();
            String userRole = String.valueOf(loggedInUser.getUserRole());
            model.addAttribute("nickname", nickname);  // 모델에 닉네임 추가
            model.addAttribute("nickname", nickname);  // 모델에 닉네임 추가
            log.info("Logged in user's nickname: " + nickname);
            log.info("user role : " + userRole);

        }
        return "midea/mlAdminRegister";
    }

    @PostMapping("/mlAdminRegister")
    public String register(MindlistAdminDTO dto, RedirectAttributes redirectAttributes, Model model){
        log.info("dto....." + dto);

        //새로 추가된 엔티티의 번호
        Long mno = mindlistAdminService.register(dto);

        redirectAttributes.addFlashAttribute("msg", mno);

        return "redirect:/midea/mindlistAdmin";
    }

    @GetMapping({"/mlAdminRead", "/mlAdminModify"}) //수정과 삭제 모두 read()가 필요하므로, 한번에 맵핑
    public void read(long mno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {
        log.info("mno: " + mno);

        MindlistAdminDTO dto = mindlistAdminService.read(mno);

        model.addAttribute("dto", dto);
    }

    @PostMapping("/mlAdminremove")
    public String remove(long mno, RedirectAttributes redirectAttributes){

        log.info("mno: " + mno);

        mindlistAdminService.removeWithComments(mno);

        redirectAttributes.addFlashAttribute("msg", mno);

        return "redirect:/midea/mindlistAdmin";
    }

    @PostMapping("/mlAdminModify")
    public String modify(MindlistAdminDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes) {

        log.info("post modify................................................");
        log.info("dto: " + dto);

        mindlistAdminService.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("mno", dto.getMno());

        return "redirect:/midea/mlAdminRead";
    }

}