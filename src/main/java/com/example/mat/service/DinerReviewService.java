package com.example.mat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.diner.DinerDto;
import com.example.mat.dto.diner.DinerImageDto;
import com.example.mat.dto.diner.DinerReviewDto;
import com.example.mat.entity.Image;
import com.example.mat.entity.diner.Diner;
import com.example.mat.entity.diner.DinerReview;
import com.example.mat.entity.shin.Member;

public interface DinerReviewService {
    // 리뷰 등록
    Long insertReview(DinerReviewDto dinerReviewDto);

    // 리뷰 목록 조회
    PageResultDto<DinerReviewDto, Object[]> getDinerReviews(PageRequestDto pageRequestDto);

    DinerReviewDto getDinerReview(Long rvid);

    Long updateDinerReview(DinerReviewDto dinerReviewDto);

    void deleteDinerReview(Long rvid);

    default DinerReviewDto entityToDto(DinerReview dinerReview, List<Image> images) {
        DinerReviewDto dinerReviewDto = DinerReviewDto.builder()
                .rvid(dinerReview.getRvid())
                .content(dinerReview.getContent())
                .tasteScore(dinerReview.getTasteScore())
                .priceScore(dinerReview.getPriceScore())
                .serviceScore(dinerReview.getServiceScore())
                .did(dinerReview.getDiner().getDid())
                .mid(dinerReview.getMember().getMid())
                .nickname(dinerReview.getMember().getNickname())
                .regDate(dinerReview.getRegDate())
                .updateDate(dinerReview.getUpdateDate())
                .build();

        List<DinerImageDto> dinerImageDtos = images.stream().map(image -> {
            return DinerImageDto.builder()
                    .inum(image.getInum())
                    .uuid(image.getUuid())
                    .imgName(image.getImgName())
                    .imgCate(image.getImgCate())
                    .path(image.getPath())
                    .build();
        }).collect(Collectors.toList());

        dinerReviewDto.setDinerImageDtos(dinerImageDtos);

        return dinerReviewDto;
    }

    default Map<String, Object> dtoToEntity(DinerReviewDto dinerReviewDto) {

        Map<String, Object> resultMap = new HashMap<>();

        DinerReview dinerReview = DinerReview.builder()
                .rvid(dinerReviewDto.getRvid())
                .content(dinerReviewDto.getContent())
                .tasteScore(dinerReviewDto.getTasteScore())
                .priceScore(dinerReviewDto.getPriceScore())
                .serviceScore(dinerReviewDto.getServiceScore())
                .diner(Diner.builder().did(dinerReviewDto.getDid()).build())
                .member(Member.builder().mid(dinerReviewDto.getMid()).build())
                .build();

        resultMap.put("dinerReview", dinerReview);

        List<DinerImageDto> dinerImageDtos = dinerReviewDto.getDinerImageDtos();

        if (dinerImageDtos != null && dinerImageDtos.size() > 0) {
            List<Image> images = dinerImageDtos.stream().map(dto -> {
                Image image = Image.builder()
                        .uuid(dto.getUuid())
                        .imgName(dto.getImgName())
                        .path(dto.getPath())
                        .imgCate(dto.getImgCate())
                        .dinerReview(dinerReview)
                        .build();
                return image;
            }).collect(Collectors.toList());

            resultMap.put("dinerReviewImages", images);
        }
        return resultMap;
    }
}
