package org.astrologist.midea.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.astrologist.midea.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchMindlistRepositoryImpl extends QuerydslRepositorySupport implements SearchMindlistRepository {

    public SearchMindlistRepositoryImpl(){
        super(Mindlist.class);
    }

    @Override
    public Mindlist search1(){

        log.info("Search1....");

        QMindlist mindlist = QMindlist.mindlist;
        QComment comment = QComment.comment;
        QUser user = QUser.user;


        JPQLQuery<Mindlist> jpqlQuery = from(mindlist);
//        jpqlQuery.leftJoin(user).on(mindlist.userIdx.eq(user);
        jpqlQuery.leftJoin(user).on(mindlist.userIdx.eq(mindlist.userIdx));
        jpqlQuery.leftJoin(comment).on(comment.mindlist.eq(mindlist));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(mindlist, user, comment.count());
        tuple.groupBy(mindlist);

        log.info("---------------------------");
        log.info(tuple);
        log.info("---------------------------");

        List<Tuple> result = tuple.fetch();

        log.info(result);

        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        log.info("search page.....");

        QMindlist mindlist = QMindlist.mindlist;
        QComment comment = QComment.comment;
        QUser user = QUser.user;

        JPQLQuery<Mindlist>jpqlQuery = from(mindlist);
        jpqlQuery.leftJoin(user).on(mindlist.userIdx.eq(mindlist.userIdx));
        jpqlQuery.leftJoin(comment).on(comment.mindlist.eq(mindlist));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(mindlist, user, comment.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression booleanExpression = mindlist.mno.gt(0L);

        booleanBuilder.and(booleanExpression);

        if(type != null){
            String[] typeArr = type.split("");
            //검색 조건 작성
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for (String t:typeArr){
                switch (t){
                    case "c": //작곡가
                        conditionBuilder.or(mindlist.composer.contains(keyword));
                        break;
                    case "t": //제목
                        conditionBuilder.or(mindlist.title.contains(keyword));
                        break;
                    case "ct": //내용
                        conditionBuilder.or(mindlist.content.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }
        tuple.where(booleanBuilder);

        tuple.groupBy(mindlist);

        this.getQuerydsl().applyPagination(pageable, tuple);

        List<Tuple> result = tuple.fetch();

        log.info(result);

        long count = tuple.fetchCount();

        log.info("COUNT: " + count);

        return new PageImpl<Object[]>(
                result.stream().map(Tuple::toArray).collect(Collectors.toList()),
                pageable,
                count);
    }

}
