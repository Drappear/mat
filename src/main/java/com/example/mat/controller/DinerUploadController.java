package com.example.mat.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.example.mat.dto.UploadResultDto;
import com.example.mat.dto.diner.DinerDto;
import com.example.mat.dto.diner.DinerImageDto;
import com.example.mat.service.DinerService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Log4j2
@RequestMapping("/dfup")
@RequiredArgsConstructor
@Controller
public class DinerUploadController {

    @Value("${com.example.mat.upload.path}")
    private String uploadPath;

    private final DinerService dinerService;

    @PostMapping("/upload")
    public ResponseEntity<List<DinerImageDto>> postUpload(MultipartFile[] uploadFiles, String uploadPage, Long did) {
        // 저장된 파일 정보 추가
        List<DinerImageDto> dinerImageDtos = new ArrayList<>();

        for (MultipartFile multipartFile : uploadFiles) {
            log.info("OriginalFilename {}", multipartFile.getOriginalFilename());
            log.info("Size {}", multipartFile.getSize());
            log.info("ContentType {}", multipartFile.getContentType()); // image/png

            // 이미지 파일 여부 확인
            if (!multipartFile.getContentType().startsWith("image")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            // 사용자가 올린 파일명
            String originName = multipartFile.getOriginalFilename();

            // 저장 폴더 생성
            String saveFolderPath = makeFolder(uploadPage, did);

            // 파일명 중복 해결 - UUID
            String uuid = UUID.randomUUID().toString();

            String saveName = saveFolderPath + File.separator + uuid + "_" + originName;

            Path savePath = Paths.get(saveName);

            try {
                // 폴더 저장
                multipartFile.transferTo(savePath);
                log.info("uploadCon 이미지 파일 저장 완료");

            } catch (Exception e) {
                e.printStackTrace();
            }

            DinerImageDto dinerImageDto = new DinerImageDto();
            dinerImageDto.setDid(did);
            dinerImageDto.setPath(savePath.toString());
            dinerImageDtos.add(dinerImageDto);

        }
        DinerDto dinerDto = dinerService.getDinerDetail(did);
        dinerDto.setDinerImageDtos(dinerImageDtos);
        dinerService.updateDiner(dinerDto);
        return new ResponseEntity<List<DinerImageDto>>(dinerImageDtos, HttpStatus.OK);
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName, String size) {
        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName = URLDecoder.decode(fileName, "utf-8");
            File file = new File(srcFileName);

            if (size != null && size.equals("1")) {
                file = new File(file.getParent(), file.getName().substring(2));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content_Type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers,
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @PostMapping("/remove")
    public ResponseEntity<String> postUploadFileRemove(String filePath, Long did) {
        log.info("삭제 요청 {}", filePath);
        try {
            String srcFileName = URLDecoder.decode(filePath, "utf-8");

            // 원본 파일 삭제
            File file = new File(srcFileName);
            file.delete();

            dinerService.deleteDinerImage(srcFileName.substring(0, srcFileName.length() - 1));

            return new ResponseEntity<>("success", HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    private String makeFolder(String uploadPageName, Long uploadId) {
        String dirStr = "c:\\upload\\" + uploadPageName + "/" + uploadId;

        File dirs = new File(dirStr);

        // 생성할 폴더 존재 여부
        if (!dirs.exists()) {
            dirs.mkdirs();
            log.info(dirStr + " 폴더 생성");
        }

        return dirStr;
    }
}
