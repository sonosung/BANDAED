package org.astrologist.midea.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.astrologist.midea.entity.Guestbook;
import org.astrologist.midea.entity.QGuestbook;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    public void insertDummies() {
        IntStream.rangeClosed(1,300).forEach(i-> {

            Guestbook guestbook = Guestbook.builder()
                    .title("Title...." + i)
                    .content("Content..." +i)
                    .writer("user" + (i % 10))
                    .build();
            System.out.println(guestbookRepository.save(guestbook));
        });
    }

    @Test
    public void updateTest() {

        Optional<Guestbook> result = guestbookRepository.findById(300L);

        if(result.isPresent()){

            Guestbook guestbook = result.get();

            guestbook.changeTitle("Title has changed..");
            guestbook.changeContent("Content has changed..");

            guestbookRepository.save(guestbook);

        }
    }

    @Test
    public void testQuery1(){

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook; //1

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder(); //2

        BooleanExpression expression = qGuestbook.title.contains(keyword); //3

        builder.and(expression); //4

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable); //5

        result.stream().forEach((guestbook -> {
            System.out.println(guestbook);
        }));
    }

    @Test
    public void testQuery2(){

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestbook.title.contains(keyword); //제목에 포함된 키워드.

        BooleanExpression exContent = qGuestbook.content.contains(keyword); //내용에 포함된 키워드.

        BooleanExpression exAll = exTitle.or(exContent); //타이틀과 내용 둘중에 하나라도 포함되있을 경우.

        builder.and(exAll);

        builder.and(qGuestbook.gno.gt(250L)); // gt 는 greater than 연산자. gno가 250L 큰 숫자의 게시물이 조회됨.

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable); //5

        result.stream().forEach((guestbook -> {
            System.out.println(guestbook);
        }));
    }

}
