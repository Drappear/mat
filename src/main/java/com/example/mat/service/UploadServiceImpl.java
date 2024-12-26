package com.example.mat.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.mat.dto.UploadResultDto;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;

@Service
@Log4j2
public class UploadServiceImpl implements UploadService {

    @Value("${com.example.mat.upload.path}")
    private String uploadPath;

    @Override
    public UploadResultDto saveFile(MultipartFile file) throws Exception {
        // 이미지 파일 확인
        if (!file.getContentType().startsWith("image")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }

        // 원본 파일명
        String originName = file.getOriginalFilename();
        if (originName == null) {
            throw new IllegalArgumentException("파일 이름을 확인할 수 없습니다.");
        }

        // 저장 경로 생성
        String folderPath = makeFolder();
        String uuid = UUID.randomUUID().toString();
        String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + originName;

        Path savePath = Paths.get(saveName);

        // 파일 저장
        file.transferTo(savePath.toFile());

        // 썸네일 생성
        String thumbSaveName = uploadPath + File.separator + folderPath + File.separator + "s_" + uuid + "_"
                + originName;
        File thumbFile = new File(thumbSaveName);
        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 100, 100);

        // 업로드 결과 반환
        return new UploadResultDto(uuid, originName, folderPath);
    }

    private String makeFolder() {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        File folder = new File(uploadPath, dateStr.replace("/", File.separator));
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return dateStr;
    }
}
