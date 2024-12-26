package com.example.mat.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.mat.dto.UploadResultDto;

public interface UploadService {
    UploadResultDto saveFile(MultipartFile file) throws Exception;

}
