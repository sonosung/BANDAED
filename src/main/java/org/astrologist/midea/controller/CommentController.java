package org.astrologist.midea.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.astrologist.midea.dto.CommentDTO;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments/")
@Log4j2
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService; //자동 주입을 위해 final

    @Autowired
    HttpSession session;

    @GetMapping(value = "/mindlist/{mno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentDTO>> getListByMindlist(@PathVariable("mno") Long mno){

        log.info("mno : " + mno);

        return new ResponseEntity<>( commentService.getList(mno), HttpStatus.OK);
    }

//    @GetMapping(value = "/mindlistAdmin/{mno}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<CommentDTO>> getListByMindlistAdmin(@PathVariable("mno") Long mno){
//
//        log.info("mno : " + mno);
//
//        return new ResponseEntity<>( commentService.getMaList(mno), HttpStatus.OK);
//    }

    //댓글 작성란에 사용자 정보 등록.
    @GetMapping("/getNickname")
    public ResponseEntity<Map<String, Object>> getNickname(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            response.put("nickname", "Guest");
        } else {
            response.put("nickname", loggedInUser.getNickname());
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> register(@RequestBody CommentDTO commentDTO, Model model){

        Map<String, Object> response = new HashMap<>();

        // 현재 로그인한 사용자 정보 가져와서, 로그인 하지 않았으면 로그인 페이지로 이동시킴.
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
//            model.addAttribute("commenter", commentDTO.getCommenter());
//            model.addAttribute("commenter", "Guest");
            response.put("nickname", "Guest");
        } else {
//            String nickname = loggedInUser.getNickname();
//            model.addAttribute("nickname", nickname);  // 모델에 닉네임 추가
            String nickname = loggedInUser.getNickname();
            response.put("nickname", nickname);
            log.info("Logged in user's nickname: " + nickname);
            log.info("user role : " + loggedInUser.getUserRole());
        }

        log.info(commentDTO);

        Long cno = commentService.register(commentDTO);
        response.put("cno", cno);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{cno}")
    public ResponseEntity<String> remove(@PathVariable("cno") Long cno) {

        log.info("cno:" + cno );

        commentService.remove(cno);

        return new ResponseEntity<>("success", HttpStatus.OK);

    }

    @PutMapping("/{cno}")
    public ResponseEntity<String> modify(@RequestBody CommentDTO commentDTO) {

        log.info(commentDTO);

        commentService.modify(commentDTO);

        return new ResponseEntity<>("success", HttpStatus.OK);

    }
}
