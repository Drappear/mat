package com.example.mat.config;

import com.example.mat.entity.won.Board;
import com.example.mat.entity.won.BoardCategory;
import com.example.mat.entity.won.BoardImage;
import com.example.mat.repository.BoardCategoryRepository;
import com.example.mat.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final BoardCategoryRepository boardCategoryRepository;
    private final BoardRepository boardRepository;

    @Override
    public void run(String... args) throws Exception {
        if (boardCategoryRepository.count() == 0) {
            BoardCategory category1 = BoardCategory.builder().name("General").build();
            BoardCategory category2 = BoardCategory.builder().name("Announcements").build();
            boardCategoryRepository.save(category1);
            boardCategoryRepository.save(category2);
        }

        if (boardRepository.count() == 0) {
            BoardCategory generalCategory = boardCategoryRepository.findByName("General");
            BoardCategory announcementCategory = boardCategoryRepository.findByName("Announcements");

            // 기본 이미지 업로드 처리
            String defaultImagePath = saveDefaultImage("default_image.jpg");

            boardRepository.save(Board.builder()
                    .title("Welcome to the Board!")
                    .content("This is the first post in the General category.")
                    .nick("Admin")
                    .viewCount(0L)
                    .boardCategory(generalCategory)
                    .image(BoardImage.builder()
                            .imgName(defaultImagePath)
                            .build())
                    .build());

            boardRepository.save(Board.builder()
                    .title("Announcements")
                    .content("This is the first announcement.")
                    .nick("Admin")
                    .viewCount(0L)
                    .boardCategory(announcementCategory)
                    .image(BoardImage.builder()
                            .imgName(defaultImagePath)
                            .build())
                    .build());
        }
    }

    private String saveDefaultImage(String fileName) throws IOException {
        Path uploadDir = Paths.get("C:/upload"); // 업로드 디렉토리 설정
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        Path defaultImage = uploadDir.resolve(fileName);
        Files.copy(getClass().getResourceAsStream("/static/images/" + fileName), defaultImage,
                StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }
}
