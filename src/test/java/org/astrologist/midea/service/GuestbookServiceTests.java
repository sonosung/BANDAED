package org.astrologist.midea.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.astrologist.midea.dto.GuestbookDTO;
import org.astrologist.midea.dto.PageRequestDTO;
import org.astrologist.midea.dto.PageResultDTO;
import org.astrologist.midea.entity.Guestbook;

@SpringBootTest
public class GuestbookServiceTests {

    @Autowired
    private GuestbookService service;

    @Test
    public void testRegister() {

        GuestbookDTO guestbookDTO = GuestbookDTO.builder()
                .title("Sample Title...")
                .content("Sample Content...")
                .writer("user0")
                .build();

        System.out.println(service.register(guestbookDTO));
    }

    @Test
    public void testList(){
        //첫번째 페이지의 목록 10개 출력
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(pageRequestDTO);

        System.out.println("PREV (이전 페이지): " + resultDTO.isPrev());
        System.out.println("NEXT (다음 페이지): " + resultDTO.isNext());
        System.out.println("TOTAL (전체 페이지): " + resultDTO.getTotalPage());
        System.out.println("------------------ 반복문으로 게시물을 불러옴 ------------------------");

        for (GuestbookDTO guestbookDTO : resultDTO.getDtoList()) {
            System.out.println(guestbookDTO);
        }

        System.out.println("------------------ 화면에 출력 될 페이지 번호 -----------------------");
        resultDTO.getPageList().forEach(i -> System.out.println(i));

    }

    @Test
    public void testSearch(){

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("tc") //검색조건 t,c,w,tc,twc..
                .keyword("한글")
                .build();

        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(pageRequestDTO);

        System.out.println("PREV: " + resultDTO.isPrev());
        System.out.println("NEXT: " + resultDTO.isNext());
        System.out.println("TOTAL: " + resultDTO.getTotalPage());

        System.out.println("----------------------------------------------------------------");
        for (GuestbookDTO guestbookDTO : resultDTO.getDtoList()){
            System.out.println(guestbookDTO);
        }

        System.out.println("================================================================");
        resultDTO.getPageList().forEach( i -> System.out.println(i));
    }

}
