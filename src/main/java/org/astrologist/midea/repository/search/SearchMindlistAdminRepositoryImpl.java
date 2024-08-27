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
public class SearchMindlistAdminRepositoryImpl extends QuerydslRepositorySupport implements SearchMindlistAdminRepository {

    public SearchMindlistAdminRepositoryImpl(){
        super(MindlistAdmin.class);
    }

    @Override
    public MindlistAdmin search1(){

        log.info("Search1....");

        QMindlistAdmin mindlistAdmin = QMindlistAdmin.mindlistAdmin;
        QComment comment = QComment.comment;
        QUser user = QUser.user;


        JPQLQuery<MindlistAdmin> jpqlQuery = from(mindlistAdmin);
        jpqlQuery.leftJoin(user).on(mindlistAdmin.userIdx.eq(mindlistAdmin.userIdx));
        jpqlQuery.leftJoin(comment).on(comment.mindlistAdmin.eq(mindlistAdmin));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(mindlistAdmin, user.email, comment.count());
        tuple.groupBy(mindlistAdmin);

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

        QMindlistAdmin mindlistAdmin = QMindlistAdmin.mindlistAdmin;
        QComment comment = QComment.comment;
        QUser user = QUser.user;

        JPQLQuery<MindlistAdmin>jpqlQuery = from(mindlistAdmin);
        jpqlQuery.leftJoin(user).on(mindlistAdmin.userIdx.eq(mindlistAdmin.userIdx));
        jpqlQuery.leftJoin(comment).on(comment.mindlistAdmin.eq(mindlistAdmin));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(mindlistAdmin, user, comment.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression booleanExpression = mindlistAdmin.mno.gt(0L);

        booleanBuilder.and(booleanExpression);

        if(type != null){
            String[] typeArr = type.split("");
            //검색 조건 작성
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for (String t:typeArr){
                switch (t){
                    case "c": //작곡가
                        conditionBuilder.or(mindlistAdmin.composer.contains(keyword));
                        break;
                    case "t": //제목
                        conditionBuilder.or(mindlistAdmin.title.contains(keyword));
                        break;
                    case "n": //작성자
                        conditionBuilder.or(mindlistAdmin.nickname.contains(keyword));
                        break;
                    case "ct": //내용
                        conditionBuilder.or(mindlistAdmin.content.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }
        tuple.where(booleanBuilder);

        tuple.groupBy(mindlistAdmin);

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