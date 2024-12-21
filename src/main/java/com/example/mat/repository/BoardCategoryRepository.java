package com.example.mat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mat.entity.won.BoardCategory;

/**
 * 게시판 카테고리 Repository
 * Spring Data JPA를 사용하여 게시판 카테고리 데이터를 관리합니다.
 */
public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Long> {

    // 기본적인 CRUD 기능은 JpaRepository를 통해 자동으로 제공됩니다.

    /**
     * 추가적으로 특정 카테고리 이름으로 검색하려면 아래 메서드를 정의할 수 있습니다.
     * 
     * @param name 카테고리 이름
     * @return 카테고리에 해당하는 엔티티
     */
    BoardCategory findByName(String name);
}
