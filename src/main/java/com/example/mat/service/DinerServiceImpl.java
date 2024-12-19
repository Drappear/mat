package com.example.mat.service;

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
import com.example.mat.dto.diner.DinerDto;
import com.example.mat.entity.diner.Diner;
import com.example.mat.entity.diner.DinerImage;
import com.example.mat.repository.DinerImageRepository;
import com.example.mat.repository.DinerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
public class DinerServiceImpl implements DinerService {

    private final DinerRepository dinerRepository;
    private final DinerImageRepository dinerImageRepository;

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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDinerDetail'");
    }

    @Override
    public Long updateDiner(DinerDto dinerDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateDiner'");
    }

    @Override
    public void deleteDiner(Long did) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteDiner'");
    }

    // @Override
    // public PageResultDto<DinerDto, Object[]> getList(PageRequestDto
    // pageRequestDto) {
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method 'getList'");
    // }

}
