package com.example.mat.repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.example.mat.entity.diner.Diner;
import com.example.mat.entity.diner.DinerImage;
import com.example.mat.entity.diner.QDiner;
import com.example.mat.entity.diner.QDinerImage;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

public class DinerImageRepositoryImpl extends QuerydslRepositorySupport implements DinerImageReviewRepository {
    public DinerImageRepositoryImpl() {
        super(DinerImage.class);
    }

    @Override
    public List<Object[]> getDinerRow(Long did) {
      QDinerImage dinerImage = QDinerImage.dinerImage;
      // QReview review = QReview.review;
      QDiner diner = QDiner.diner;

      JPQLQuery<DinerImage> query = from(dinerImage).leftJoin(diner).on(diner.eq(dinerImage.diner));

      // JPQLQuery<Long> rCnt = JPAExpressions.select(review.countDistinct()).from(review)
      //                 .where(review.diner.eq(dinerImage.diner));
      // JPQLQuery<Double> rAvg = JPAExpressions.select(review.grade.avg().round()).from(review)
      //                 .where(review.diner.eq(dinerImage.diner));

      JPQLQuery<Tuple> tuple = query.select(diner, dinerImage)
                      .where(dinerImage.diner.did.eq(did))
                      .orderBy(dinerImage.inum.desc());

      List<Tuple> result = tuple.fetch();

      return result.stream().map(t -> t.toArray()).collect(Collectors.toList());
    }

    @Override
    public Page<Object[]> getTotalList(String type, String keyword, Pageable pageable) {
        QDinerImage dinerImage = QDinerImage.dinerImage;
        // QReview review = QReview.review;
        QDiner diner = QDiner.diner;

        JPQLQuery<DinerImage> query = from(dinerImage).leftJoin(diner).on(diner.eq(dinerImage.diner));

        // JPQLQuery<Long> rCnt =
        // JPAExpressions.select(review.countDistinct()).from(review)
        // .where(review.movie.eq(movieImage.movie));
        // JPQLQuery<Double> rAvg =
        // JPAExpressions.select(review.grade.avg().round()).from(review)
        // .where(review.movie.eq(movieImage.movie));

        JPQLQuery<Long> inum = JPAExpressions.select(
                dinerImage.inum.max()).from(
                        dinerImage)
                .groupBy(dinerImage.diner);

        JPQLQuery<Tuple> tuple = query.select(
                diner,
                dinerImage)
                .where(dinerImage.inum.in(inum));

        // bno > 0 조건
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(diner.did.gt(0L));

        if (type != null && type.trim().length() != 0) {

            // 식당명 검색
            BooleanBuilder conditionBuilder = new BooleanBuilder();
            if (type.contains("n")) {
                conditionBuilder.or(diner.name.contains(keyword));
            }

            builder.and(conditionBuilder);
        }

        tuple.where(builder);

        // sort
        Sort sort = pageable.getSort();
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();

            PathBuilder<Diner> orderByExpression = new PathBuilder<>(Diner.class, "diner");

            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();
        long count = tuple.fetchCount();

        return new PageImpl<>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable,
                count);
    }
}
