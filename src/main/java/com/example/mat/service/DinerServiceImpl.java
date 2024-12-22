package com.example.mat.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.diner.DinerCategoryDto;
import com.example.mat.dto.diner.DinerDto;
import com.example.mat.entity.diner.Diner;
import com.example.mat.entity.diner.DinerCategory;
import com.example.mat.entity.diner.DinerImage;
import com.example.mat.repository.DinerCategoryRepository;
import com.example.mat.repository.DinerImageRepository;
import com.example.mat.repository.DinerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
public class DinerServiceImpl implements DinerService {

    private final DinerRepository dinerRepository;
    private final DinerImageRepository dinerImageRepository;
    private final DinerCategoryRepository dinerCategoryRepository;

    @Override
    public Long createDiner(DinerDto dinerDto) {
        Map<String, Object> entityMap = dtoToEntity(dinerDto);

        Diner diner = (Diner) entityMap.get("diner");
        List<DinerImage> dinerImages = (List<DinerImage>) entityMap.get("dinerImages");

        dinerRepository.save(diner);
        dinerImages.forEach(dinerImage -> dinerImageRepository.save(dinerImage));

        return diner.getDid();
    }

    @Override
    public DinerDto getDinerDetail(Long did) {
        List<Object[]> result = dinerImageRepository.getDinerRow(did);

        Diner diner = (Diner) result.get(0)[0];
        // Long reviewCnt = (Long) result.get(0)[2];
        // Double reviewAvg = (Double) result.get(0)[3];

        // 식당 이미지
        List<DinerImage> dinerImages = new ArrayList<>();
        result.forEach(row -> {
          DinerImage dinerImage = (DinerImage) row[1];
          dinerImages.add(dinerImage);
        });

        return entityToDto(diner, dinerImages);
    }

    @Transactional
    @Override
    public Long updateDiner(DinerDto dinerDto) {
      Map<String, Object> entityMap = dtoToEntity(dinerDto);

      Diner diner = (Diner) entityMap.get("diner");
      List<DinerImage> dinerImages = (List<DinerImage>) entityMap.get("dinerImages");

      dinerRepository.save(diner);

      // 기존 영화 이미지 제거
      dinerImageRepository.deleteByDiner(diner);

      dinerImages.forEach(dinerImage -> dinerImageRepository.save(dinerImage));

      return diner.getDid();
    }

    @Transactional
    @Override
    public void deleteDiner(Long did) {
      Diner diner = Diner.builder().did(did).build();
      dinerImageRepository.deleteByDiner(diner);
      // reviewRepository.deleteByDiner(diner);
      dinerRepository.delete(diner);
    }

    @Override
    public PageResultDto<DinerDto, Object[]> getDinerList(PageRequestDto pageRequestDto) {
        Pageable pageable = pageRequestDto.getPageable(Sort.by("did").descending());
        
        Page<Object[]> result = dinerImageRepository.getTotalList(pageRequestDto.getType(), pageRequestDto.getKeyword(),
                pageable);

        Function<Object[], DinerDto> function = (en -> entityToDto((Diner) en[0],
                (List<DinerImage>) Arrays.asList((DinerImage) en[1])));

        return new PageResultDto<>(result, function);
    }

    @Override
    public List<DinerCategoryDto> getCategoryList() {
      List<DinerCategory> result = dinerCategoryRepository.findAll();

        return result.stream().map(c -> entityToDto(c)).collect(Collectors.toList());
    }

}
