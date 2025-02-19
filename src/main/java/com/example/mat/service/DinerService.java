package com.example.mat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.diner.DinerCategoryDto;
import com.example.mat.dto.diner.DinerDto;
import com.example.mat.dto.diner.DinerImageDto;
import com.example.mat.entity.Image;
import com.example.mat.entity.diner.Diner;
import com.example.mat.entity.diner.DinerCategory;

public interface DinerService {

    // 식당 목록 조회
    PageResultDto<DinerDto, Object[]> getDinerList(PageRequestDto pageRequestDto);

    // 식당 등록
    Long createDiner(DinerDto dinerDto);

    // 식당 상세 조회
    DinerDto getDinerDetail(Long did);

    // 식당 수정
    Long updateDiner(DinerDto dinerDto);

    // 식당 삭제
    void deleteDiner(Long did);

    // 식당 이미지 삭제
    void deleteDinerImage(String filePath);

    List<DinerCategoryDto> getCategoryList();

    String getCategoryName(String dcid);

    public default DinerCategoryDto entityToDto(DinerCategory entity) {
        return DinerCategoryDto.builder().dcid(entity.getDcid()).name(entity.getName()).build();
    }

    public default DinerCategory dtoToEntity(DinerCategoryDto dto) {
        return DinerCategory.builder().dcid(dto.getDcid()).name(dto.getName()).build();
    }

    default DinerDto entityToDto(Diner diner, List<Image> dinerImages) {
        DinerDto dinerDto = DinerDto.builder()
                .did(diner.getDid())
                .name(diner.getName())
                .address(diner.getAddress())
                .addressDetail(diner.getAddressDetail())
                .categoryName(Long.toString(diner.getDinerCategory().getDcid()))
                .content(diner.getContent())
                .menu(diner.getMenu())
                .workTime(diner.getWorkTime())
                .phone(diner.getPhone())
                .regNum(diner.getRegNum())
                .regDate(diner.getRegDate())
                .build();

        List<DinerImageDto> dinerImageDtos = dinerImages.stream().map(dinerImage -> {
            return DinerImageDto.builder()
                    .inum(dinerImage.getInum())
                    .path(dinerImage.getPath())
                    .build();
        }).collect(Collectors.toList());

        dinerDto.setDinerImageDtos(dinerImageDtos);

        return dinerDto;
    }

    default Map<String, Object> dtoToEntity(DinerDto dinerDto) {
        Map<String, Object> resultMap = new HashMap<>();

        Diner diner = Diner.builder()
                .did(dinerDto.getDid())
                .name(dinerDto.getName())
                .address(dinerDto.getAddress())
                .addressDetail(dinerDto.getAddressDetail())
                .content(dinerDto.getContent())
                .menu(dinerDto.getMenu())
                .workTime(dinerDto.getWorkTime())
                .phone(dinerDto.getPhone())
                .regNum(dinerDto.getRegNum())
                .viewCount(dinerDto.getViewCount())
                .dinerCategory(DinerCategory.builder().dcid(Long.parseLong(dinerDto.getCategoryName())).build())
                .build();

        resultMap.put("diner", diner);

        List<DinerImageDto> dinerImageDtos = dinerDto.getDinerImageDtos();

        if (dinerImageDtos != null && dinerImageDtos.size() > 0) {
            List<Image> images = dinerImageDtos.stream().map(dto -> {
                Image image = Image.builder()
                        .path(dto.getPath())
                        .diner(diner)
                        .build();
                return image;
            }).collect(Collectors.toList());

            resultMap.put("dinerImages", images);
        }

        return resultMap;
    }

    void createDinerImage(DinerImageDto dinerImageDto);

}
