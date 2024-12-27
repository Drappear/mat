package com.example.mat.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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
    public List<DinerReviewDto> getDinerReviews(Long did) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDinerReviews'");
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
