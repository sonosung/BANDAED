package org.astrologist.midea.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.astrologist.midea.dto.CommentDTO;
import org.astrologist.midea.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comments/")
@Log4j2
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService; //자동 주입을 위해 final

    @GetMapping(value = "/mindlist/{mno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentDTO>> getListByMindlist(@PathVariable("mno") Long mno){

        log.info("mno : " + mno);

        return new ResponseEntity<>( commentService.getList(mno), HttpStatus.OK);
    }

    @GetMapping(value = "/mindlistAdmin/{mno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentDTO>> getListByMindlistAdmin(@PathVariable("mno") Long mno){

        log.info("mno : " + mno);

        return new ResponseEntity<>( commentService.getList(mno), HttpStatus.OK);
    }
}
