package com.example.mat.service;

import com.example.mat.dto.won.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {

    /**
     * 게시물 등록
     *
     * @param boardDto 게시물 데이터
     * @return 등록된 게시물 ID
     */
    Long register(BoardDto boardDto);

    /**
     * 게시물 등록 (이미지 포함)
     *
     * @param boardDto 게시물 데이터
     * @param file     이미지 파일
     * @return 등록된 게시물 ID
     */
    Long registerWithImage(BoardDto boardDto, MultipartFile file);

    /**
     * 게시물 수정
     *
     * @param boardDto 수정할 게시물 데이터
     * @return 수정된 게시물 ID
     */
    Long modify(BoardDto boardDto);

    /**
     * 게시물 수정 (이미지 포함)
     *
     * @param boardDto 수정할 게시물 데이터
     * @param file     이미지 파일
     * @return 수정된 게시물 ID
     */
    Long modifyWithImage(BoardDto boardDto, MultipartFile file);

    /**
     * 게시물 삭제
     *
     * @param bno 게시물 ID
     */
    void delete(Long bno);

    /**
     * 게시물 상세 조회
     *
     * @param bno 게시물 ID
     * @return 게시물 데이터
     */
    BoardDto getDetail(Long bno);

    /**
     * 게시물 목록 조회
     *
     * @return 게시물 데이터 목록
     */
    List<BoardDto> getList();

    /**
     * 검색 및 페이징을 포함한 게시물 목록 조회
     *
     * @param keyword  검색 키워드
     * @param category 카테고리 ID
     * @param pageable 페이징 정보
     * @return 페이징 처리된 게시물 데이터
     */
    Page<BoardDto> getList(String keyword, Long category, Pageable pageable);
}
