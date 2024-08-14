package org.astrologist.midea.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.astrologist.midea.entity.Mindlist;
import org.astrologist.midea.entity.QMindlist;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MindlistRepositoryTests {

    @Autowired
    private MindlistRepository mindlistRepository;

    @Test
    public void insertDummies() {
        IntStream.rangeClosed(1,5).forEach(i-> {

            Mindlist mindlist = Mindlist.builder()
                    .composer("composer"+i)
                    .title("Title...." + i)
                    .url("usl..." +i)
                    .content("content.."+i)
                    .nickname("nickname" + (i % 10))
                    .build();
            System.out.println(mindlistRepository.save(mindlist));
        });
    }

    @Test
    public void updateTest() {

        Optional<Mindlist> result = mindlistRepository.findById(20L);

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
    public void testQuery1(){

        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());

        QMindlist qMindlist = QMindlist.mindlist; //1

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder(); //2

        BooleanExpression expression = qMindlist.composer.contains(keyword); //3

        builder.and(expression); //4

        Page<Mindlist> result = mindlistRepository.findAll(builder, pageable); //5

        result.stream().forEach((mindlist -> {
            System.out.println(mindlist);
        }));
    }

    @Test
    public void testQuery2(){

        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());

        QMindlist qMindlist = QMindlist.mindlist;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exTitle = qMindlist.composer.contains(keyword); //제목에 포함된 키워드.

        BooleanExpression exContent = qMindlist.url.contains(keyword); //내용에 포함된 키워드.

        BooleanExpression exAll = exTitle.or(exContent); //타이틀과 내용 둘중에 하나라도 포함되있을 경우.

        builder.and(exAll);

        builder.and(qMindlist.mno.gt(5L)); // gt 는 greater than 연산자. mno가 250L 큰 숫자의 게시물이 조회됨.

        Page<Mindlist> result = mindlistRepository.findAll(builder, pageable); //5

        result.stream().forEach((mindlist -> {
            System.out.println(mindlist);
        }));
    }

}
