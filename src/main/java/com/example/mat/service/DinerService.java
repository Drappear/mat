package com.example.mat.service;

import com.example.mat.dto.diner.DinerDto;
import com.example.mat.entity.diner.Diner;

public interface DinerService {

    // 식당 등록
    Long createDiner(DinerDto dinerDto);

    // 식당 상세조회
    DinerDto getDinerDetail(Long did);

    // 식당 수정
    Long updateDiner(DinerDto dinerDto);

    // 식당 삭제
    void deleteDiner(Long did);

    default DinerDto entityToDto(Diner diner) {
        DinerDto dinerDto = DinerDto.builder()
                .did(diner.getDid())
                .name(diner.getName())
                .address(diner.getAddress())
                .content(diner.getContent())
                .menu(diner.getMenu())
                .workTime(diner.getWorkTime())
                .phone(diner.getPhone())
                .regNum(diner.getRegNum())
                .viewCount(diner.getViewCount())
                .build();

        return dinerDto;
    }

    default Diner dtoToEntity(DinerDto dinerDto) {
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

        return diner;
    }
}
