package org.astrologist.midea.service;

import org.astrologist.midea.dto.CommentDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CommentServiceTests {

    @Autowired
    private CommentService service;

    //------------------------------------------유저 게시판 댓글 리스트------------------------------------------
    @Test
    public void testGetList() {
        Long mno = 1L;

        List<CommentDTO> commentDTOList = service.getList(mno);

        commentDTOList.forEach(commentDTO -> System.out.println(commentDTO));
    }


    //------------------------------------------관리자 게시판 댓글 리스트------------------------------------------
//    @Test
//    public void testGetMaList() {
//        Long mno = 1L;
//
//        List<CommentDTO> commentDTOList = service.getMaList(mno);
//
//        commentDTOList.forEach(commentDTO -> System.out.println(commentDTO));
//    }
}
