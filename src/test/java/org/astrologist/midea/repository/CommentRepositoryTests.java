package org.astrologist.midea.repository;

import org.astrologist.midea.entity.Comment;
import org.astrologist.midea.entity.Mindlist;
import org.astrologist.midea.entity.MindlistAdmin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class CommentRepositoryTests {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void insertComment(){

        IntStream.rangeClosed(1, 300).forEach(i -> {
            //1부터 100까지의 임의의 번호를 이용.
            long mno = (long)(Math.random() * 100) + 1;

            Mindlist mindlist = Mindlist.builder().mno(mno).build();

            Comment comment = Comment.builder()
                    .text("sounds good to me.." + i)
                    .mindlist(mindlist)
                    .commenter("commenter" + i) //comment 테이블의 칼럼명.
                    .build();

            commentRepository.save(comment);
        });
    }

    @Transactional
    @Test
    public void readComment1() {
        Optional<Comment> result = commentRepository.findById(100L);

        Comment comment = result.get();

        System.out.println(comment);
        System.out.println(comment.getMindlist());

    }

    @Test
    public void testListByMindlist() {
        List<Comment> commentList = commentRepository.getCommentByMindlistOrderByCno(
                Mindlist.builder().mno(2L).build());

        commentList.forEach(comment -> System.out.println(comment));
    }
}
