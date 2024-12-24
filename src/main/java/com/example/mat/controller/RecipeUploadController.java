package com.example.mat.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.mat.dto.UploadResultDto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@RequestMapping("/upload")
@Controller
public class RecipeUploadController {

  // @Value("${com.example.mat.upload.path}")
  // private String uploadPath;

  // @GetMapping("/upload")
  // public void getUpload() {
  //   log.info("업로드 폼 요청");
  // }

  // @PostMapping("/upload")
  // public ResponseEntity<List<UploadResultDto>> postUpload(MultipartFile[] uploadFiles) {
  //   // 저장된 파일 정보 추가
  //   List<UploadResultDto> uploadResultDtos = new ArrayList<>();

  //   for (MultipartFile multipartFile : uploadFiles) {
  //     log.info("OriginalFilename {}", multipartFile.getOriginalFilename());
  //     log.info("Size {}", multipartFile.getSize());
  //     log.info("ContentType {}", multipartFile.getContentType()); // image/png

  //     // 이미지 파일 여부 확인
  //     if (!multipartFile.getContentType().startsWith("image")) {
  //       return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  //     }
  //     // 사용자가 올린 파일명
  //     String originName = multipartFile.getOriginalFilename();

  //     // 년/월/일
  //     String saveFolderPath = makeFolder();

  //     // 파일저장 - uuid(중복파일 해결)
  //     String uuid = UUID.randomUUID().toString();
  //     // upload/2024/11/26/9fae42cf-0733-453f-b3b9-3bfca31a6fe2_1.jpg
  //     String saveName = uploadPath + File.separator + saveFolderPath + File.separator + uuid + "_" + originName;

  //     Path savePath = Paths.get(saveName);

  //     try {
  //       // 폴더 저장
  //       multipartFile.transferTo(savePath);

  //       // // 썸네일 저장
  //       // String thumbSaveName = uploadPath + File.separator + saveFolderPath + File.separator + "s_" + uuid + "_"
  //       //     + originName;
  //       // File thumbFile = new File(thumbSaveName);
  //       // Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 100, 100);

  //     } catch (Exception e) {
  //       e.printStackTrace();
  //     }

  //     uploadResultDtos.add(new UploadResultDto(uuid, originName, saveFolderPath));
  //   }
  //   return new ResponseEntity<List<UploadResultDto>>(uploadResultDtos, HttpStatus.OK);
  // }

  // @GetMapping("/display")
  // public ResponseEntity<byte[]> getFile(String fileName, String size) {
  //   ResponseEntity<byte[]> result = null;

  //   try {
  //     // "2024%2F11%2F27%5C7e9547c0-ba45-463b-a4ae-59a35d92962a_seoul1.jpg"
  //     String srcFileName = URLDecoder.decode(fileName, "utf-8");
  //     // upload/2024/11/27/s_C7e9547c0-ba45-463b-a4ae-59a35d92962a_seoul1.jpg
  //     File file = new File(uploadPath + File.separator + srcFileName);

  //     if (size != null && size.equals("1")) {
  //       // upload/2024/11/27/, 원본파일명
  //       file = new File(file.getParent(), file.getName().substring(2));
  //     }

  //     HttpHeaders headers = new HttpHeaders();
  //     // Content-Type : image/png or text/html
  //     headers.add("Content-Type", Files.probeContentType(file.toPath()));
  //     result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);
  //   } catch (Exception e) {
  //     e.printStackTrace();
  //     return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  //   }
  //   return result;
  // }

  // @PostMapping("/remove")
  // public ResponseEntity<String> postRemove(String filePath) {
  //   log.info("삭제 요청 {}", filePath);

  //   try {
  //     String srcFileName = URLDecoder.decode(filePath, "utf-8");

  //     // 원본 파일 삭제
  //     File file = new File(uploadPath, srcFileName);
  //     file.delete();

  //     // // 썸네일 파일 삭제
  //     // // /upload/2024/11/27/~~~~~_1.jpg
  //     // File thumbFile = new File(file.getParent(), "s_" + file.getName());
  //     // thumbFile.delete();

  //     return new ResponseEntity<>("success", HttpStatus.OK);

  //   } catch (UnsupportedEncodingException e) {
  //     e.printStackTrace();
  //     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  //   }
  // }

  // private String makeFolder() {
  //   // 오늘날짜 구하기
  //   LocalDate today = LocalDate.now();
  //   log.info("today {}", today); // 2024-11-26
  //   String dateStr = today.format(DateTimeFormatter.ofPattern("YYYY/MM/dd"));

  //   File dirs = new File(uploadPath, dateStr);
  //   if (!dirs.exists()) {
  //     dirs.mkdirs(); // 실제 폴더 생성
  //   }

  //   // 폴더구조 : / or \\
  //   // c:/upload/1.jpg or c:\\upload\\1.jpg

  //   // 날짜나 시간, 숫자 특정 포맷을 지정해 보고 싶다? Formatter
  //   // SimpleDateFormat sdf = new SimpleDateFormat("YYYY/mm/dd");
  //   // sdf.format(new Date());

  //   // 오늘날짜로 폴더 생성
  //   return dateStr;
  // }

}