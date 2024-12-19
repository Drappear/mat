package com.example.mat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.diner.DinerDto;
import com.example.mat.dto.diner.DinerImageDto;
import com.example.mat.entity.diner.Diner;
import com.example.mat.entity.diner.DinerImage;

public interface DinerService {

    // 식당 목록 조회
    // PageResultDto<DinerDto, Object[]> getList(PageRequestDto pageRequestDto);

    // 식당 등록
    Long createDiner(DinerDto dinerDto);

    // 식당 상세 조회
    DinerDto getDinerDetail(Long did);

    // 식당 수정
    Long updateDiner(DinerDto dinerDto);

    // 식당 삭제
    void deleteDiner(Long did);

    default DinerDto entityToDto(Diner diner, Long viewCount) {
        DinerDto dinerDto = DinerDto.builder()
                .did(diner.getDid())
                .name(diner.getName())
                .address(diner.getAddress())
                .content(diner.getContent())
                .menu(diner.getMenu())
                .workTime(diner.getWorkTime())
                .phone(diner.getPhone())
                .regNum(diner.getRegNum())
                .viewCount(viewCount)
                .build();

        return dinerDto;
    }

    default Map<String, Object> dtoToEntity(DinerDto dinerDto) {
        Map<String, Object> resultMap = new HashMap<>();

        Diner diner = Diner.builder()
                .did(dinerDto.getDid())
                .name(dinerDto.getName())
                .address(dinerDto.getAddress())
                .content(dinerDto.getContent())
                .menu(dinerDto.getMenu())
                .workTime(dinerDto.getWorkTime())
                .phone(dinerDto.getPhone())
                .regNum(dinerDto.getRegNum())
                .viewCount(dinerDto.getViewCount())
                .build();

        resultMap.put("diner", diner);

        List<DinerImageDto> dinerImageDtos = dinerDto.getDinerImageDtos();

        if (dinerImageDtos != null && dinerImageDtos.size() > 0) {
            List<DinerImage> dinerImages = dinerImageDtos.stream().map(dto -> {
                DinerImage dinerImage = DinerImage.builder()
                        .uuid(dto.getUuid())
                        .imgName(dto.getImgName())
                        .path(dto.getPath())
                        .imgCate(dto.getImgCate())
                        .diner(diner)
                        .build();
                return dinerImage;
            }).collect(Collectors.toList());

            resultMap.put("dinerImages", dinerImages);
        }

        return resultMap;
    }
}
