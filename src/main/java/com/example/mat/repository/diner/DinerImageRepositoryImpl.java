package com.example.mat.repository.diner;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.example.mat.entity.Image;
import com.example.mat.entity.QImage;
import com.example.mat.entity.diner.Diner;
import com.example.mat.entity.diner.DinerReview;
import com.example.mat.entity.diner.QDiner;
import com.example.mat.entity.diner.QDinerReview;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DinerImageRepositoryImpl extends QuerydslRepositorySupport
        implements DinerImageRepository {
    public DinerImageRepositoryImpl() {
        super(Image.class);
    }

    @Override
    public List<Object[]> getDinerRow(Long did) {
        QImage image = QImage.image;
        QDiner diner = QDiner.diner;

        JPQLQuery<Image> query = from(image).leftJoin(diner).on(diner.eq(image.diner));

        JPQLQuery<Tuple> tuple = query.select(diner, image)
                .where(image.diner.did.eq(did).and(image.path.substring(10, 17).contains("diner")))
                .orderBy(image.inum.desc());

        List<Tuple> result = tuple.fetch();

        return result.stream().map(t -> t.toArray()).collect(Collectors.toList());
    }

    @Override
    public Page<Object[]> getTotalDinerList(String type, String keyword, Pageable pageable) {
        QImage image = QImage.image;
        // QReview review = QReview.review;
        QDiner diner = QDiner.diner;

        JPQLQuery<Image> query = from(image).leftJoin(diner).on(diner.eq(image.diner));

        // JPQLQuery<Long> rCnt =
        // JPAExpressions.select(review.countDistinct()).from(review)
        // .where(review.movie.eq(movieImage.movie));
        // JPQLQuery<Double> rAvg =
        // JPAExpressions.select(review.grade.avg().round()).from(review)
        // .where(review.movie.eq(movieImage.movie));

        JPQLQuery<Long> inum = JPAExpressions.select(
                image.inum.max()).from(
                        image)
                .where(image.path.substring(10, 17).contains(
                        "diner"))
                .groupBy(image.diner);

        JPQLQuery<Tuple> tuple = query.select(
                diner,
                image)
                .where(image.inum.in(inum));

        // bno > 0 조건
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(diner.did.gt(0L));

        if (type != null && type.trim().length() != 0) {

            // 식당명 검색
            BooleanBuilder conditionBuilder = new BooleanBuilder();
            if (type.contains("n")) {
                conditionBuilder.or(diner.name.contains(keyword));
            } else if (type.contains("c")) {
                conditionBuilder.or(diner.dinerCategory.name.contains(keyword));
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

    @Override
    public void deleteByDiner(Diner diner) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteByDiner'");
    }

    // 리뷰 목록
    @Override
    public Page<Object[]> getTotalReviewList(Pageable pageable, Long did) {
        QImage image = QImage.image;
        QDinerReview dinerReview = QDinerReview.dinerReview;

        JPQLQuery<Image> query = from(image).leftJoin(dinerReview).on(dinerReview.eq(image.dinerReview));

        JPQLQuery<Tuple> tuple = query.select(
                dinerReview,
                image)
                .where(image.path.substring(10, 17).contains("review").and(dinerReview.diner.did.eq(did)))
                .orderBy(image.inum.desc());

        // rvid > 0 조건
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(dinerReview.rvid.gt(0L));

        tuple.where(builder);

        // sort
        Sort sort = pageable.getSort();
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();

            PathBuilder<DinerReview> orderByExpression = new PathBuilder<>(DinerReview.class, "dinerReview");

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
