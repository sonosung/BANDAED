package org.astrologist.midea.service;

import org.astrologist.midea.dto.MindlistDTO;
import org.astrologist.midea.dto.PageRequestDTO;
import org.astrologist.midea.dto.PageResultDTO;
import org.astrologist.midea.entity.Mindlist;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MindlistServiceTests {

    @Autowired
    private MindlistService service;

    @Test
    public void testRegister() {

        MindlistDTO mindlistDTO = MindlistDTO.builder()
                .composer("Sample Composer...")
                .title("Sample Title...")
                .url("Sample URL...")
                .content("Sample Content..")
                .nickname("Nickname")
                .build();

        System.out.println(service.register(mindlistDTO));
    }

    @Test
    public void testList() {
        //첫번째 페이지의 목록 10개 출력
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        PageResultDTO<MindlistDTO, Mindlist> resultDTO = service.getList(pageRequestDTO);

        System.out.println("PREV (이전 페이지): " + resultDTO.isPrev());
        System.out.println("NEXT (다음 페이지): " + resultDTO.isNext());
        System.out.println("TOTAL (전체 페이지): " + resultDTO.getTotalPage());
        System.out.println("------------------ 반복문으로 게시물을 불러옴 ------------------------");

        for (MindlistDTO mindlistDTO : resultDTO.getDtoList()) {
            System.out.println(mindlistDTO);
        }

        System.out.println("------------------ 화면에 출력 될 페이지 번호 -----------------------");
        resultDTO.getPageList().forEach(i -> System.out.println(i));

    }

    @Test
    public void testSearch() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("tc") //검색조건 t,c,w,tc,twc..
                .keyword("com")
                .build();

        PageResultDTO<MindlistDTO, Mindlist> resultDTO = service.getList(pageRequestDTO);

        System.out.println("PREV: " + resultDTO.isPrev());
        System.out.println("NEXT: " + resultDTO.isNext());
        System.out.println("TOTAL: " + resultDTO.getTotalPage());

        System.out.println("----------------------------------------------------------------");
        for (MindlistDTO mindlistDTO : resultDTO.getDtoList()) {
            System.out.println(mindlistDTO);
        }

        System.out.println("================================================================");
        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }

}

