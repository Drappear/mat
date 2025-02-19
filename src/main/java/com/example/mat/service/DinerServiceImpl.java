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
import com.example.mat.dto.diner.DinerImageDto;
import com.example.mat.entity.Image;
import com.example.mat.entity.diner.Diner;
import com.example.mat.entity.diner.DinerCategory;
import com.example.mat.repository.ImageRepository;
import com.example.mat.repository.diner.DinerCategoryRepository;
import com.example.mat.repository.diner.DinerRepository;
import com.example.mat.repository.diner.DinerReviewImageRepository;
import com.example.mat.repository.diner.DinerReviewRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
public class DinerServiceImpl implements DinerService {

  private final DinerRepository dinerRepository;
  private final DinerReviewRepository dinerReviewRepository;
  private final DinerReviewImageRepository dinerReviewImageRepository;
  private final ImageRepository imageRepository;
  private final DinerCategoryRepository dinerCategoryRepository;

  @Override
  public Long createDiner(DinerDto dinerDto) {
    Map<String, Object> entityMap = dtoToEntity(dinerDto);
    Diner diner = (Diner) entityMap.get("diner");

    dinerRepository.save(diner);

    return diner.getDid();
  }

  @Override
  public DinerDto getDinerDetail(Long did) {
    List<Object[]> result = imageRepository.getDinerRow(did);

    Diner diner = (Diner) result.get(0)[0];

    // 식당 이미지
    List<Image> dinerImages = new ArrayList<>();
    result.forEach(row -> {
      Image dinerImage = (Image) row[1];
      dinerImages.add(dinerImage);
    });

    return entityToDto(diner, dinerImages);
  }

  @Transactional
  @Override
  public Long updateDiner(DinerDto dinerDto) {
    Map<String, Object> entityMap = dtoToEntity(dinerDto);

    Diner diner = (Diner) entityMap.get("diner");
    List<Image> dinerImages = (List<Image>) entityMap.get("dinerImages");

    dinerRepository.save(diner);

    imageRepository.deleteByDiner(diner);

    dinerImages.forEach(dinerImage -> imageRepository.save(dinerImage));

    return diner.getDid();
  }

  @Transactional
  @Override
  public void deleteDiner(Long did) {
    Diner diner = Diner.builder().did(did).build();
    log.info("리뷰 이미지 삭제");
    dinerReviewImageRepository.deleteByDiner(diner);

    log.info("리뷰 삭제");
    dinerReviewRepository.deleteByDiner(diner);

    log.info("식당 이미지 삭제");
    imageRepository.deleteByDiner(diner);

    log.info("식당 삭제");
    dinerRepository.delete(diner);
  }

  @Override
  public PageResultDto<DinerDto, Object[]> getDinerList(PageRequestDto pageRequestDto) {
    Pageable pageable = pageRequestDto.getPageable(Sort.by("did").descending());

    Page<Object[]> result = imageRepository.getTotalDinerList(pageRequestDto.getType(),
        pageRequestDto.getKeyword(),
        pageable);

    Function<Object[], DinerDto> function = (en -> entityToDto((Diner) en[0],
        (List<Image>) Arrays.asList((Image) en[1])));

    return new PageResultDto<>(result, function);
  }

  @Override
  public List<DinerCategoryDto> getCategoryList() {
    List<DinerCategory> result = dinerCategoryRepository.findAll();

    return result.stream().map(c -> entityToDto(c)).collect(Collectors.toList());
  }

  @Transactional
  @Override
  public void deleteDinerImage(String filePath) {
    log.info("식당 이미지 삭제 : {}", filePath);
    imageRepository.deleteByPath(filePath);
  }

  @Override
  public String getCategoryName(String dcid) {
    log.info("카테고리 id : {}", dcid);
    return dinerCategoryRepository.findById(Long.parseLong(dcid)).get().getName();
  }

  @Transactional
  @Override
  public void createDinerImage(DinerImageDto dinerImageDto) {
    DinerDto dinerDto = getDinerDetail(dinerImageDto.getDid());
    List<DinerImageDto> dinerImageDtos = dinerDto.getDinerImageDtos();
    dinerImageDtos.add(dinerImageDto);
    dinerDto.setDinerImageDtos(dinerImageDtos);
    updateDiner(dinerDto);
    log.info("이미지 정보 업데이트");
  }

}
