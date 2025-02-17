package com.example.mat.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.mat.dto.diner.DinerImageDto;
import com.example.mat.dto.recipe.RecipeImageDto;
import com.example.mat.entity.Image;
import com.example.mat.entity.recipe.RecipeImage;
import com.example.mat.repository.ImageRepository;
import com.example.mat.repository.RecipeImageRepository;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class FileCheckTask {

  @Autowired
  private RecipeImageRepository recipeImageRepository;

  @Autowired
  private ImageRepository imageRepository;

  @Value("${com.example.mat.upload.path}")
  private String uploadPath;

  private String getYesterDayFolder() {

    LocalDate yesterday = LocalDate.now().minusDays(1);
    String result = yesterday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    return result.replace("-", File.separator);
  }

  // second,minute,hour,day of month,month,day of week
  @Scheduled(cron = "0 0 2 * * *")
  public void checkFile() {
    log.info("file check 메소드 실행");

    // db에서 전일자 이미지 파일 목록 추출
    List<RecipeImage> oldRecipeImages = recipeImageRepository.findOldFileAll();
    List<Image> oldImages = imageRepository.findOldFileAll();

    // entity => dto
    List<RecipeImageDto> recipeImageDtos = oldRecipeImages.stream().map(recipeImage -> {
      return RecipeImageDto.builder()
          .rInum(recipeImage.getRInum())
          .uuid(recipeImage.getUuid())
          .imgName(recipeImage.getImgName())
          .path(recipeImage.getPath())
          .build();
    }).collect(Collectors.toList());

    List<DinerImageDto> dinerImageDtos = oldImages.stream().map(dinerImage -> {
      return DinerImageDto.builder()
          .inum(dinerImage.getInum())
          .path(dinerImage.getPath())
          .build();
    }).collect(Collectors.toList());

    // uplod/2024/12/03/~~~~~~_1.jpg
    List<Path> filePaths = recipeImageDtos.stream()
        .map(dto -> Paths.get(uploadPath, dto.getImageURL(), dto.getUuid() + "_" + dto.getImgName()))
        .collect(Collectors.toList());
    List<Path> dinerFilePaths = dinerImageDtos.stream()
        .map(dto -> Paths.get(uploadPath, dto.getImageURL(), dto.getUuid() + "_" + dto.getImgName()))
        .collect(Collectors.toList());

    // uplod/2024/12/03/s_~~~~~~_1.jpg
    recipeImageDtos.stream()
        .map(dto -> Paths.get(uploadPath, dto.getImageURL(), "s_" + dto.getUuid() + "_" + dto.getImgName()))
        .forEach(p -> filePaths.add(p));

    // 어제날짜의 파일 목록 추출
    File targetDir = Paths.get(uploadPath, getYesterDayFolder()).toFile();
    File[] removeFiles = targetDir.listFiles(f -> filePaths.contains(f.toPath()) == false);
    File[] removeDinerFiles = targetDir.listFiles(f -> dinerFilePaths.contains(f.toPath()) == false);

    // 비교후 db 목록과 일치하지 않는 파일 제거
    for (File file : removeFiles) {
      log.info("remove File {}", file.getAbsolutePath()); // 삭제될 것
      file.delete();
    }
    for (File file : removeDinerFiles) {
      log.info("remove File {}", file.getAbsolutePath());
      file.delete();
    }

  }

}