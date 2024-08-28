package org.astrologist.midea.repository;

import org.astrologist.midea.entity.Mindlist;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MindlistRepositoryTests {

    @Autowired
    private MindlistRepository mindlistRepository;

    @Test
    public void insertDummies() {

        IntStream.rangeClosed(1, 100).forEach(i -> {

            User user = User.builder()
//                    .id(Long.valueOf(i))
                    .email("testemail" + i + "@naver.com")
                    .password("testpassword" + i)  // 패스워드 설정
                    .nickname("testnickname" + i)  // 닉네임 설정
                    .build();

            Mindlist mindlist = Mindlist.builder()
                    .composer("composer" + i)
                    .content("content.." + i)
//                    .userIdx(User.builder().email("user" + i + "@naver.com").build())
//                    .userIdx(User.builder().nickname("nickname" + i).build())
                    .nickname(user.getNickname())
//                    .userIdx(user)
                    .title("title...." + i)
                    .url("url..." + i)
                    .likeCount(i)
                    .happy(i % 2 == 0)
                    .sad(i % 3 == 0)
                    .calm(i % 4 == 0)
                    .stressed(i % 5 == 0)
                    .joyful(i % 6 == 0)
                    .energetic(i % 7 == 0)
                    .build();

            mindlistRepository.save(mindlist);
        });
    }

    @Transactional
    @Test
    public void testRead1() {

        Optional<Mindlist> result = mindlistRepository.findById(100L);

        Mindlist mindlist = result.get();

        System.out.println(mindlist);
//        System.out.println(mindlist.getNickname());
        System.out.println(mindlist.getUserIdx());
    }

    @Test
    public void testReadWithWriter() {

        Object result = mindlistRepository.getMindlistWithWriter(100L);

        Object[] arr = (Object[])result;

        System.out.println("-------------------------------");
        System.out.println(Arrays.toString(arr));

    }

    @Transactional
    @Test
    public void testGetMindlistWithReply() {

        List<Object[]> result = mindlistRepository.getMindlistWithComment(100L);

        for (Object[] arr : result) {
            System.out.println(Arrays.toString(arr));
        }
    }

    @Transactional
    @Test
    public void testWithCommentCount() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());

        Page<Object[]> result = mindlistRepository.getMindlistWithCommentCount(pageable);

        result.get().forEach(row -> {
            Object[] arr = (Object[])row;

            System.out.println(Arrays.toString(arr));
        });
    }

    @Test
    public void testRead3() {

        Object result = mindlistRepository.getMindlistByMno(100L);

        Object[] arr = (Object[])result;

        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void updateTest() {

        Optional<Mindlist> result = mindlistRepository.findById(2L);

        if(result.isPresent()){

            Mindlist mindlist = result.get();

            mindlist.changeComposer("Composer has changed..");
            mindlist.changeUrl("URL has changed..");
            mindlist.changeTitle("Title has changed..");
            mindlist.changeContent(("content has changed.."));

            mindlistRepository.save(mindlist);

        }
    }

    @Test
    public void testSearchPage() {

        Pageable pageable =
                PageRequest.of(0,10,
                        Sort.by("mno").descending()
                                .and(Sort.by("title").ascending()));

        Page<Object[]> result = mindlistRepository.searchPage("t", "1", pageable);

    }

}
