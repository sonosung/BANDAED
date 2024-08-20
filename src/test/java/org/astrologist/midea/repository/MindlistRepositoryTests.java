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
        IntStream.rangeClosed(1,100).forEach(i-> {

            User user = User.builder().nickname("nickname" + i).build();

            Mindlist mindlist = Mindlist.builder()
                    .composer("composer"+i)
                    .content("content.."+i)
                    .nickname(user)
                    .title("title...." + i)
                    .url("url..." +i)
                    .calm(true)
                    .happy(true)
                    .sad(true)
                    .stressed(true)
                    .energetic(true)
                    .joyful(true)
                    .build();

            mindlistRepository.save(mindlist);
        });
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
    public void testReadWithWriter() {

        Object result = mindlistRepository.getMindlistdWithWriter(100L);

        Object[] arr = (Object[])result;

        System.out.println("-------------------------------");
        System.out.println(Arrays.toString(arr));

    }

    @Test
    public void testSearchPage() {

        Pageable pageable =
                PageRequest.of(0,10,
                        Sort.by("mno").descending()
                                .and(Sort.by("title").ascending()));

        Page<Object[]> result = mindlistRepository.searchPage("t", "1", pageable);

    }

    @Test
    public void testGetMindlistWithReply() {

        List<Object[]> result = mindlistRepository.getMindlistWithComment(100L);

        for (Object[] arr : result) {
            System.out.println(Arrays.toString(arr));
        }
    }

}
