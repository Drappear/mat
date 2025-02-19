package com.example.mat.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.diner.DinerDto;
import com.example.mat.dto.diner.DinerImageDto;
import com.example.mat.dto.diner.DinerReviewDto;
import com.example.mat.service.DinerReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Log4j2
@RequestMapping("/diner")
@RestController
public class DinerReviewController {
    private final DinerReviewService dinerReviewService;

    @Value("${com.example.mat.upload.path}")
    private String uploadPath;

    @PostMapping("/review")
    public Long postDinerReview(@RequestBody DinerReviewDto dinerReviewDto, @RequestParam(value = "uploadFiles") MultipartFile[] uploadFiles, String uploadPage, Long did) {
        log.info("리뷰 등록 {}", dinerReviewDto);

        Long rvid = dinerReviewService.insertReview(dinerReviewDto);
        log.info("리뷰 등록됨 : {}", rvid);

        List<DinerImageDto> dinerImageDtos = new ArrayList<>();

        for (MultipartFile multipartFile : uploadFiles) {

            // 사용자가 올린 파일명
            String originName = multipartFile.getOriginalFilename();
            log.info("파일명 :  {}", originName);

            // 저장 폴더 생성
            String saveFolderPath = makeFolder("review", did);

            // 파일명 중복 해결 - UUID
            String uuid = UUID.randomUUID().toString();

            String saveName = saveFolderPath + File.separator + uuid + "_" + originName;

            Path savePath = Paths.get(saveName);

            try {
                // 폴더에 파일 저장
                multipartFile.transferTo(savePath);
                log.info("파일 저장 완료");
                DinerImageDto dinerImageDto = new DinerImageDto();
                dinerImageDto.setRvid(rvid);
                dinerImageDto.setPath(savePath.toString());
                dinerImageDtos.add(dinerImageDto);

            } catch (Exception e) {
                e.printStackTrace();
                log.info("파일 저장 실패");
                dinerReviewService.deleteDinerReview(rvid);
                removeFolder("review", did);
            }

        }
        dinerReviewDto.setDinerImageDtos(dinerImageDtos);
        dinerReviewDto.setRvid(rvid);
        dinerReviewDto.setDid(did);
        dinerReviewService.updateDinerReview(dinerReviewDto);
        return rvid;
    }

    @GetMapping("/{did}/all")
    public PageResultDto<DinerReviewDto, Object[]> getReviewList(@PathVariable Long did,
            @ModelAttribute("reviewRequestDto") PageRequestDto pageRequestDto, Model model) {
        log.info("리뷰 리스트 요청 {}", did);

        PageResultDto<DinerReviewDto, Object[]> review = dinerReviewService.getDinerReviews(pageRequestDto, did);
        log.info("리뷰 리스트 {}", review);

        // model.addAttribute("review", review);

        return review;
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

  private void removeFolder(String uploadPageName, Long uploadId) {
      String dirStr = "c:\\upload\\" + uploadPageName + "/" + uploadId;

      File dirs = new File(dirStr);

      // 삭제할 폴더 존재 여부
      while (dirs.exists()) {
          File[] files = dirs.listFiles();

          // 하위 파일 삭제
          for (File file : files) {
              file.delete();
          }

          if (files.length == 0 && dirs.isDirectory()) { // 하위 파일이 없는지와 폴더인지 확인 후 폴더 삭제
              dirs.delete();
              log.info(dirStr + " 폴더 삭제");
          }
      }
  }
}
