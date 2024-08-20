package org.astrologist.midea.service;

import org.astrologist.midea.dto.MindlistAdminDTO;
import org.astrologist.midea.dto.PageRequestDTO;
import org.astrologist.midea.dto.PageResultDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SpringBootTest
public class MindlistAdminServiceTests {

    @Autowired
    private MindlistAdminService mindlistAdminService;

    @Test
    public void testRegister() {

        MindlistAdminDTO mindlistAdminDTO = MindlistAdminDTO.builder()
                .composer("composer")
                .content("content..")
                .email("admin1@naver.com")
                .title("title....")
                .url("url...")
                .calm(true)
                .happy(true)
                .sad(true)
                .stressed(true)
                .energetic(true)
                .joyful(true)
                .build();

        Long mno = mindlistAdminService.register(mindlistAdminDTO);

    }

    @Test
    public void testList() {

        //1페이지 10개
        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResultDTO<MindlistAdminDTO, Object[]> result = MindlistAdminService.getList(pageRequestDTO);

        for (MindlistAdminDTO mindlistAdminDTO : result.getDtoList()) {
            System.out.println(mindlistAdminDTO);
        }

    }

    @Test
    public void testRead() {

        Long mno = 100L;

        MindlistAdminDTO mindlistAdminDTO = MindlistAdminService.read(mno);

        System.out.println(mindlistAdminDTO);
    }

    @Test
    public void testRemove() {

        Long mno = 1L;

        mindlistAdminService.removeWithComments(mno);

    }

    @Test
    public void testModify() {

        MindlistAdminDTO mindlistAdminDTO = MindlistAdminDTO.builder()
                .mno(2L)
                .title("제목 변경합니다.2")
                .content("내용 변경합니다.2")
                .build();

        MindlistAdminService.modify(mindlistAdminDTO);

    }


    @Test
    public void testSearch(){

        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setPage(1);
        pageRequestDTO.setSize(10);
        pageRequestDTO.setType("t");
        pageRequestDTO.setKeyword("11");

        Pageable pageable = pageRequestDTO.getPageable(Sort.by("mno").descending());

        PageResultDTO<MindlistAdminDTO, Object[]> result = MindlistAdminService.getList(pageRequestDTO);

        for (MindlistAdminDTO mindlistAdminDTO : result.getDtoList()) {
            System.out.println(mindlistAdminDTO);
        }
    }
}
