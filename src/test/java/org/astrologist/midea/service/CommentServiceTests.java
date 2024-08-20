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

    @Test
    public void testGetList() {
        Long mno = 100L;

        List<CommentDTO> commentDTOList = service.getList(mno);

        commentDTOList.forEach(commentDTO -> System.out.println(commentDTO));
    }
}
