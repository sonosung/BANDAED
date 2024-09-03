package org.astrologist.midea.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.astrologist.midea.dto.*;
import org.astrologist.midea.entity.Mindlist;
import org.astrologist.midea.entity.MindlistAdmin;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.repository.MindlistAdminRepository;
import org.astrologist.midea.repository.MindlistRepository;
import org.astrologist.midea.service.MindlistAdminService;
import org.astrologist.midea.service.MindlistService;
import org.hibernate.boot.MappingNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.astrologist.midea.dto.MindlistDTO;

import java.util.Map;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/midea")
@Log4j2
@RequiredArgsConstructor //자동 주입을 위한 Annotation
public class MindlistAdminController {

    private final MindlistAdminService mindlistAdminService; //MindlistService 인터페이스를 final로 구현.
    private final MindlistAdminRepository repository;

    @Autowired
    private HttpSession session; // 현재 사용자의 세션을 주입받습니다.

    @GetMapping("/mindlistAdmin")
    public void list(PageRequestDTO pageRequestDTO, Model model, AlgorithmRequestDTO algorithmRequestDTO, User user, UserDTO userDTO){

        String userImage = userDTO.getProfileImagePath();

        User loggedInUser = (User) session.getAttribute("user");
        /*좋아요 위치 바꿈*/
        model.addAttribute("result", mindlistAdminService.getList(pageRequestDTO));
        model.addAttribute("algorithm", mindlistAdminService.getAlgorithmList(algorithmRequestDTO));

        if (loggedInUser != null) {
            String nickname = loggedInUser.getNickname();
            model.addAttribute("nickname", nickname);  // 모델에 닉네임 추가
            log.info("Logged in user's nickname: " + nickname);

            /*좋아요 상태 리스트에 덮에씌우기*/
            PageResultDTO<MindlistAdminDTO, Object[]> resultWithLikes = mindlistAdminService.getListWithLikes(pageRequestDTO, loggedInUser);
            model.addAttribute("result", resultWithLikes);  // 기존 리스트를 좋아요 상태 포함 리스트로 덮어씁니다.
        } else {
            String nickname = "GUEST";
            model.addAttribute("nickname", nickname);  // 모델에 닉네임 추가
        }

        log.info("list......................" + pageRequestDTO);

        log.info("algorithm......................" + algorithmRequestDTO);


        //유저 프로필사진 정보 가져오기.
        String profileImagePath = user.getProfileImagePath();

        if (profileImagePath == null || profileImagePath.isEmpty()) {
            profileImagePath = "/default.images/default-profile.jpg";
        } else {
            profileImagePath = userImage;
        }
        model.addAttribute("profileImage", profileImagePath);

    }

    @GetMapping("/mlAdminRegister")
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
        return "midea/mlAdminRegister";
    }

    @PostMapping("/mlAdminRegister")
    public String register(MindlistAdminDTO dto, RedirectAttributes redirectAttributes, User user){

        log.info("dto....." + dto);

        //새로 추가된 엔티티의 번호
        Long mno = mindlistAdminService.register(dto);

        redirectAttributes.addFlashAttribute("msg", mno);

        return "redirect:/midea/mindlistAdmin";
    }

    @GetMapping({"/mlAdminRead", "/mlAdminModify"}) //수정과 삭제 모두 read()가 필요하므로, 한번에 맵핑
    public String read(@ModelAttribute("requestDTO") PageRequestDTO requestDTO, Long mno, Model model, HttpServletRequest request) {

        log.info("mno: " + mno);

        MindlistAdminDTO mindlistAdminDTO = mindlistAdminService.read(mno);

        log.info(mindlistAdminDTO);

        // 현재 로그인한 사용자 정보 가져와서, 로그인 하지 않았으면 로그인 페이지로 이동시킴.
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            model.addAttribute("nickname", "GUEST");  // 모델에 닉네임 추가
        } else {
            String nickname = loggedInUser.getNickname();
            model.addAttribute("nickname", nickname);  // 모델에 닉네임 추가
            log.info("Logged in user's nickname: " + nickname);
            log.info("user role : " + loggedInUser.getUserRole());
        }

        model.addAttribute("dto", mindlistAdminDTO);

        // 현재 요청된 URL을 확인하여 리다이렉트 경로 설정
        String requestURI = request.getRequestURI();

        if (requestURI.contains("/mlAdminRead")) {
            return "midea/mlAdminRead";
        } else if (requestURI.contains("/mlAdminModify")) {
            return "midea/mlAdminModify";
        }

        // 기본적으로 mlread 페이지로 리다이렉트
        return "midea/mlAdminRead";
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

        log.info("post modify....");
        log.info("dto: " + dto);

        mindlistAdminService.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("mno", dto.getMno());

        return "redirect:/midea/mlAdminRead";
    }

    /*좋아요 기능을 포함한 새로운 목록 조회 메서드 추가*/
    @GetMapping("/mindlistAdminWithLikes")
    public String listWithLikes(PageRequestDTO pageRequestDTO, Model model, HttpSession session) {

        PageResultDTO<MindlistAdminDTO, Object[]> pageResultDTO = mindlistAdminService.getList(pageRequestDTO);

        List<MindlistAdminDTO> dtoList = pageResultDTO.getDtoList();

        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            // 로그인된 사용자가 '좋아요'를 누른 게시물들을 설정
            dtoList.forEach(dto -> {
                boolean liked = mindlistAdminService.checkUserLiked(dto.getMno(), currentUser);
                dto.setLiked(liked);
                System.out.println("DTO MNO: " + dto.getMno() + ", Liked: " + dto.isLiked());  // 로그 추가
            });
        }

        model.addAttribute("result", pageResultDTO);
        return "midea/mindlistAdmin";
    }
    /*좋아요*/
    @PostMapping("/mindlistAdmin/like")
    @ResponseBody
    public ResponseEntity<String> toggleLike(@RequestBody Map<String, Long> payload) {
        Long mno = payload.get("mno");
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            log.warn("Like toggle attempt without logged-in user.");
            return ResponseEntity.status(403).body("로그인이 필요합니다.");
        }

        if (mno == null) {
            log.warn("Like toggle attempt with invalid mno.");
            return ResponseEntity.badRequest().body("유효하지 않은 요청입니다. mno가 필요합니다.");
        }

        try {
            log.info("Toggling like for mno=" + mno + " by user=" + loggedInUser.getNickname());
            mindlistAdminService.toggleLike(mno, loggedInUser);
            log.info("Like status successfully toggled for mno=" + mno);
            return ResponseEntity.ok("좋아요 상태가 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            log.error("Error toggling like for mno=" + mno + ": " + e.getMessage());
            return ResponseEntity.badRequest().body("유효하지 않은 게시물입니다: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while toggling like for mno=" + mno + ": " + e.getMessage());
            return ResponseEntity.status(500).body("좋아요 상태 변경 실패: " + e.getMessage());
        }
    }
}