package org.astrologist.midea.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.astrologist.midea.dto.MindlistDTO;
import org.astrologist.midea.dto.PageRequestDTO;
import org.astrologist.midea.dto.PageResultDTO;
import org.astrologist.midea.entity.Mindlist;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.astrologist.midea.entity.QMindlist.mindlist;

@SpringBootTest
public class MindlistServiceTests {

    @Autowired
    private MindlistService mindlistService;

    @Autowired
    private Mindlist mindlist;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private HttpServletRequest request;

    @Test
    public void testRegister() {

        MindlistDTO mindlistDTO = MindlistDTO.builder()
                .composer("Sample Composer...")
                .title("Sample Title...")
                .url("Sample URL...")
                .content("Sample Content..")
                .nickname("iguana")       //현재 데이터베이스에 존재하는 유저 닉네임
//                .email("email4@naver.com")   //현재 데이터베이스에 존재하는 유저 이메일
//                .password("password2")       //현재 데이터베이스에 존재하는 유저 비밀번호
                .calm(true)
                .happy(true)
                .sad(true)
                .stressed(true)
                .energetic(true)
                .joyful(true)
                .build();

//        User user = User.builder()
//                .nickname("test nickname")
//                .password("test password")
//                .email("test email")
//                .build();

        Long mno = mindlistService.register(mindlistDTO);
    }

//    @Test
//    public void testList() {
//
//        //1페이지 10개
//        PageRequestDTO pageRequestDTO = new PageRequestDTO();
//
//        PageResultDTO<MindlistDTO, Object[]> result = mindlistService.getList(pageRequestDTO);
//
//        for (MindlistDTO mindlistDTO : result.getDtoList()) {
//            System.out.println(mindlistDTO);
//        }
//
//    }

    @Test
    public void testRead(){

        Long mno = 1L;

        MindlistDTO mindlistDTO = mindlistService.read(mno);

        System.out.println(mindlistDTO);
    }

    @Test
    public void testRemove() {

        Long mno = 1L;

        mindlistService.removeWithComments(mno);

    }

    @Test
    public void testModify() {

        MindlistDTO mindlistDTO = MindlistDTO.builder()
                .mno(2L)
                .composer("change composer")
                .content("change content")
                .title("change title")
                .url("change url")
                .happy(true)
                .sad(false)
                .calm(true)
                .stressed(false)
                .joyful(true)
                .energetic(true)
                .build();

        mindlistService.modify(mindlistDTO);
    }

}

