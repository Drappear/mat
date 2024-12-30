package com.example.mat.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.mat.dto.UploadResultDto;

import org.springframework.web.bind.annotation.PostMapping;

@Log4j2
@RequestMapping("/recipe/upload")
@RestController
public class RecipeUploadController {

    @Value("${com.example.mat.upload.path.recipe.step}")
    private String stepUploadPath;
    
    @Value("${com.example.mat.upload.path.recipe.image}")
    private String imageUploadPath;

    @PostMapping("/step")
    public ResponseEntity<UploadResultDto> uploadStepImage(@RequestParam("file") MultipartFile file) {
        return handleImageUpload(file, stepUploadPath);
    }

    @PostMapping("/image")
    public ResponseEntity<UploadResultDto> uploadRecipeImage(@RequestParam("file") MultipartFile file) {
        return handleImageUpload(file, imageUploadPath);
    }

    private ResponseEntity<UploadResultDto> handleImageUpload(MultipartFile file, String uploadPath) {
        if (!file.getContentType().startsWith("image")) {
            return ResponseEntity.badRequest().build();
        }

        try {
            String originName = file.getOriginalFilename();
            String folderPath = makeFolder(uploadPath);
            String uuid = UUID.randomUUID().toString();
            String savePath = uploadPath + File.separator + folderPath + 
                            File.separator + uuid + "_" + originName;

            file.transferTo(Paths.get(savePath));

            UploadResultDto result = new UploadResultDto(uuid, originName, folderPath);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            log.error("File upload failed", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private String makeFolder(String basePath) {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        File dirs = new File(basePath, dateStr);
        if (!dirs.exists()) {
            dirs.mkdirs();
        }
        return dateStr;
    }
}
