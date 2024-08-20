package org.astrologist.midea.service;

import org.astrologist.midea.dto.MindlistDTO;
import org.astrologist.midea.dto.PageRequestDTO;
import org.astrologist.midea.dto.PageResultDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MindlistServiceTests {

    @Autowired
    private MindlistService mindlistService;

    @Test
    public void testRegister() {

        MindlistDTO mindlistDTO = MindlistDTO.builder()
                .composer("Sample Composer...")
                .title("Sample Title...")
                .url("Sample URL...")
                .content("Sample Content..")
                .nickname("nickname")
                .build();

        Long mno = mindlistService.register(mindlistDTO);
    }

    @Test
    public void testList() {

        //1페이지 10개
        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResultDTO<MindlistDTO, Object[]> result = mindlistService.getList(pageRequestDTO);

        for (MindlistDTO mindlistDTO : result.getDtoList()) {
            System.out.println(mindlistDTO);
        }

    }

    @Test
    public void testRemove() {

        Long mno = 1L;

        mindlistService.removeWithComments(mno);

    }

    @Test
    public void testRead(){

        Long mno = 1L;

        MindlistDTO mindlistDTO = mindlistService.read(mno);

        System.out.println(mindlistDTO);
    }

}

