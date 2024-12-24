package com.example.mat.repository.RecipeTotal;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.example.mat.entity.recipe.QRecipe;
import com.example.mat.entity.recipe.QRecipeImage;
import com.example.mat.entity.recipe.Recipe;
import com.example.mat.entity.recipe.RecipeImage;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class RecipeImageReviewRepositoryImpl extends QuerydslRepositorySupport implements RecipeImageReviewRepository {

  public RecipeImageReviewRepositoryImpl() {
    super(RecipeImage.class);
  }

  @Override
  public Page<Object[]> getTotalList(String type, String keyword, Pageable pageable) {

    QRecipeImage recipeImage = QRecipeImage.recipeImage;
    // QReview review = QReview.review;
    QRecipe recipe = QRecipe.recipe;

    JPQLQuery<RecipeImage> query = from(recipeImage).leftJoin(recipe).on(recipe.eq(recipeImage.recipe));

    // JPQLQuery<Long> rCnt = JPAExpressions.select(review.countDistinct()).from(review)
    //     .where(review.movie.eq(movieImage.movie));
    // JPQLQuery<Double> rAvg = JPAExpressions.select(review.grade.avg().round()).from(review)
    //     .where(review.movie.eq(movieImage.movie));

    JPQLQuery<Long> rInum = JPAExpressions.select(recipeImage.rInum.max()).from(recipeImage)
        .groupBy(recipeImage.recipe);

    JPQLQuery<Tuple> tuple = query.select(recipe, recipeImage) //, rCnt, rAvg
        .where(recipeImage.rInum.in(rInum));

    // rno > 0 조건
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(recipe.rno.gt(0L));

    if (type != null && type.trim().length() != 0) {
      // 영화명 검색
      BooleanBuilder conditionBuilder = new BooleanBuilder();
      if (type.contains("t")) { //TODO: 검색 type 바꿔야 함 //TODO: 2024-12-24
        conditionBuilder.or(recipe.title.contains(keyword));
      }

      builder.and(conditionBuilder);
    }

    tuple.where(builder);

    // Sort
    Sort sort = pageable.getSort();
    sort.stream().forEach(order -> {
      // com.querydsl.core.types.Order
      Order direction = order.isAscending() ? Order.ASC : Order.DESC;
      String prop = order.getProperty();
      // PathBuilder : Sort 객체 속성 - bno or title 이런 것들 지정
      PathBuilder<Recipe> orderByExpression = new PathBuilder<>(Recipe.class, "recipe");
      // Sort 객체 사용 불가로 OrderSpecifier() 사용
      // com.querydsl.core.types.OrderSpecifier.OrderSpecifier(Order order, Expression
      // target)
      tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
    });

    // page 처리
    tuple.offset(pageable.getOffset());
    tuple.limit(pageable.getPageSize());

    List<Tuple> result = tuple.fetch();
    long count = tuple.fetchCount();

    return new PageImpl<>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable,
        count);
  }

  @Override
  public List<Object[]> getRecipeRow(Long rno) {

    QRecipeImage recipeImage = QRecipeImage.recipeImage;
    // QReview review = QReview.review;
    QRecipe recipe = QRecipe.recipe;

    JPQLQuery<RecipeImage> query = from(recipeImage).leftJoin(recipe).on(recipe.eq(recipeImage.recipe));

    // JPQLQuery<Long> rCnt = JPAExpressions.select(review.countDistinct()).from(review)
    //     .where(review.movie.eq(movieImage.movie));
    // JPQLQuery<Double> rAvg = JPAExpressions.select(review.grade.avg().round()).from(review)
    //     .where(review.movie.eq(movieImage.movie));

    JPQLQuery<Tuple> tuple = query.select(recipe, recipeImage) //, rCnt, rAvg
        .where(recipeImage.recipe.rno.eq(rno))
        .orderBy(recipeImage.rInum.desc());

    List<Tuple> result = tuple.fetch();

    return result.stream().map(t -> t.toArray()).collect(Collectors.toList());
  }

}