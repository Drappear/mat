package com.example.mat.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.diner.DinerImageDto;
import com.example.mat.dto.diner.DinerReviewDto;
import com.example.mat.entity.Image;
import com.example.mat.entity.diner.DinerReview;
import com.example.mat.repository.ImageRepository;
import com.example.mat.repository.diner.DinerReviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class DinerReviewServiceImpl implements DinerReviewService {

    private final DinerReviewRepository dinerReviewRepository;
    private final ImageRepository imageRepository;

    @Override
    public Long insertReview(DinerReviewDto dinerReviewDto) {
        Map<String, Object> entityMap = dtoToEntity(dinerReviewDto);
        DinerReview dinerReview = (DinerReview) entityMap.get("dinerReview");
        List<Image> dinerReviewImages = (List<Image>) entityMap.get("dinerReviewImages");

        dinerReviewRepository.save(dinerReview);
        dinerReviewImages.forEach(dinerReviewImage -> imageRepository.save(dinerReviewImage));
        return dinerReview.getRvid();
    }

    @Override
    public PageResultDto<DinerReviewDto, Object[]> getDinerReviews(PageRequestDto pageRequestDto, Long did) {
        Pageable pageable = pageRequestDto.getPageable(Sort.by("rvid").descending());

        Page<Object[]> result = imageRepository.getTotalReviewList(pageable, did);

        List<Object[]> contents = result.getContent();
        List<Image> reviewImages = new ArrayList<>();
        Function<Object[], DinerReviewDto> function = null;

        List<DinerReviewDto> dinerReviewDtos = new ArrayList<>();

        contents.forEach(en -> {
            DinerReview dinerReview = (DinerReview) en[0];

            DinerReviewDto dinnerReviewDto = DinerReviewDto.builder().rvid(dinerReview.getRvid()).build();

            if (!dinerReviewDtos.contains(dinnerReviewDto)) {
                dinerReviewDtos.add(dinnerReviewDto);
            }
        });

        contents.forEach(en -> {
            Image dinerImage = (Image) en[1];

            dinerReviewDtos.forEach(dto -> {
                if (dto.getRvid() == dinerImage.getDinerReview().getRvid()) {
                    dto.getDinerImageDtos().add(DinerImageDto.builder().inum(dinerImage.getInum()).build());
                }
            });
        });

        return new PageResultDto<>(result, dinerReviewDtos);

    }

    @Override
    public DinerReviewDto getDinerReview(Long rvid) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDinerReview'");
    }

    @Override
    public Long updateDinerReview(DinerReviewDto dinerReviewDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateDinerReview'");
    }

    @Override
    public void deleteDinerReview(Long rvid) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteDinerReview'");
    }

}
