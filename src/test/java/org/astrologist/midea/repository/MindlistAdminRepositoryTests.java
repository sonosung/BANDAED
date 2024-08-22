package org.astrologist.midea.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.astrologist.midea.entity.MindlistAdmin;
import org.astrologist.midea.entity.QMindlistAdmin;
import org.astrologist.midea.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MindlistAdminRepositoryTests {

    @Autowired
    private MindlistAdminRepository mindlistAdminRepository;

    @Test
    public void insertDummies() {
        IntStream.rangeClosed(1,100).forEach(i-> {

            User user = User.builder()
                    .email(i+ "email" + i + "@naver.com")
                    .password(i+ "password" + i)  // 패스워드 설정
                    .nickname(i+ "nickname" + i)  // 닉네임 설정
                    .build();


            MindlistAdmin mindlistAdmin = MindlistAdmin.builder()

                    .composer("composer"+i)
                    .title("Title...." + i)
                    .url("usl..." +i)
                    .content("content.."+i)
                    .email(user)
                    .calm(true)
                    .happy(true)
                    .sad(true)
                    .stressed(true)
                    .energetic(true)
                    .joyful(true)
                    .build();

            mindlistAdminRepository.save(mindlistAdmin);
        });
    }

    @Transactional
    @Test
    public void testRead1() {

        Optional<MindlistAdmin> result = mindlistAdminRepository.findById(100L); //데이터베이스에 존재하는 번호

        MindlistAdmin mindlistAdmin = result.get();

        System.out.println(mindlistAdmin);
        System.out.println(mindlistAdmin.getEmail());

    }

    @Test
    public void updateTest() {

        Optional<MindlistAdmin> result = mindlistAdminRepository.findById(20L);

        if(result.isPresent()){

            MindlistAdmin mindlistAdmin = result.get();

            mindlistAdmin.changeComposer("Composer has changed..");
            mindlistAdmin.changeUrl("URL has changed..");
            mindlistAdmin.changeTitle("Title has changed..");
            mindlistAdmin.changeContent(("content has changed.."));

            mindlistAdminRepository.save(mindlistAdmin);

        }
    }

    @Test
    public void testReadWithWriter() {

        Object result = mindlistAdminRepository.getMindlistAdminWithWriter(100L);

        Object[] arr = (Object[])result;

        System.out.println("-------------------------------");
        System.out.println(Arrays.toString(arr));

    }

    @Test
    public void testSearch1() {

        mindlistAdminRepository.search1();

    }

    @Test
    public void testSearchPage() {

        Pageable pageable =
                PageRequest.of(0,10,
                        Sort.by("mno").descending()
                                .and(Sort.by("title").ascending()));

        Page<Object[]> result = mindlistAdminRepository.searchPage("t", "1", pageable);

    }

}
