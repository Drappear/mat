package com.example.mat.repository.shin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

// import org.springframework.data.domain.Page;

// import org.springframework.data.domain.Pageable;

import com.example.mat.entity.shin.MemberImage;
import com.example.mat.entity.shin.QMember;
import com.example.mat.entity.shin.QMemberImage;

import com.querydsl.core.Tuple;

import com.querydsl.jpa.JPQLQuery;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ProfileImageRepositoryImpl extends QuerydslRepositorySupport implements ProfileImageRepository {

        public ProfileImageRepositoryImpl() {
                super(MemberImage.class);
        }

        @Override
        public List<Object[]> getMemberRow(Long mid) {

                QMemberImage memberImage = QMemberImage.memberImage;

                QMember member = QMember.member;

                JPQLQuery<MemberImage> query = from(memberImage).leftJoin(member).on(member.eq(memberImage.member));

                // JPQLQuery<Long> rCnt =
                // JPAExpressions.select(review.countDistinct()).from(review)
                // .where(review.member.eq(memberImage.member));
                // JPQLQuery<Double> rAvg =
                // JPAExpressions.select(review.grade.avg().round()).from(review)
                // .where(review.member.eq(memberImage.member));

                // JPQLQuery<Tuple> tuple = query.select(member, memberImage, rCnt, rAvg)
                JPQLQuery<Tuple> tuple = query.select(member, memberImage)
                                .where(memberImage.member.mid.eq(mid))
                                .orderBy(memberImage.inum.desc());

                List<Tuple> result = tuple.fetch();

                return result.stream().map(t -> t.toArray()).collect(Collectors.toList());
        }

}