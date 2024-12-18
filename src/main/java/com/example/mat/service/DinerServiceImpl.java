package com.example.mat.service;

import org.springframework.stereotype.Service;

import com.example.mat.dto.diner.DinerDto;
import com.example.mat.repository.DinerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
public class DinerServiceImpl implements DinerService {

    private final DinerRepository dinerRepository;

    @Override
    public Long createDiner(DinerDto dinerDto) {
        return dinerRepository.save(dtoToEntity(dinerDto)).getDid();
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

}
