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
    public PageResultDto<List<DinerReviewDto>, Object[]> getDinerReviews(PageRequestDto pageRequestDto, Long did) {
        Pageable pageable = pageRequestDto.getPageable(Sort.by("rvid").descending());

        Page<Object[]> result = imageRepository.getTotalReviewList(pageable, did);

        List<Object[]> contents = result.getContent();
        List<Image> reviewImages = new ArrayList<>();
        Function<Object[], DinerReviewDto> function = null;

        List<DinerReviewDto> dinerReviewDtos = new ArrayList<>();

        for (Object[] objects : contents) {
            System.out.println("service 확인");
            System.out.println(Arrays.toString(objects));

            DinerReview dinerReview = (DinerReview) objects[0];
            Image image = (Image) objects[1];

            DinerReviewDto dinerReviewDto = entityToDto(dinerReview, Arrays.asList(image));
            dinerReviewDtos.add(dinerReviewDto);
        }

        System.out.println("dinerReviewDtos  " + dinerReviewDtos);
        // function = (en -> entityToDto(dinerReview, reviewImages));

        // return new PageResultDto<>(result, function);
        return null;

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
