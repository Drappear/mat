package com.example.mat.service;

import com.example.mat.dto.won.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface BoardService {

    /**
     * 게시물 등록
     *
     * @param boardDto 게시물 데이터 (제목, 내용, 카테고리)
     * @param file     업로드된 이미지 파일
     * @return 등록된 게시물 ID
     * @throws IllegalArgumentException 유효하지 않은 데이터가 전달될 경우
     */
    Long register(BoardDto boardDto, MultipartFile file);

    /**
     * 게시물 수정
     *
     * @param boardDto    수정할 게시물 데이터 (수정된 파일 포함)
     * @param file        새로운 업로드된 이미지 파일 (없을 경우 기존 이미지 유지)
     * @param deleteImage 기존 이미지를 삭제할지 여부
     * @return 수정된 게시물 ID
     * @throws IllegalArgumentException 게시물 ID가 유효하지 않거나 데이터가 누락된 경우
     */
    Long modify(BoardDto boardDto, MultipartFile file, boolean deleteImage);

    /**
     * 게시물 삭제
     *
     * @param bno 게시물 ID
     * @throws IllegalArgumentException 유효하지 않은 게시물 ID일 경우
     */
    void delete(Long bno);

    /**
     * 게시물 상세 조회
     *
     * @param bno 게시물 ID
     * @return 게시물 데이터
     * @throws IllegalArgumentException 게시물 ID가 유효하지 않은 경우
     */
    BoardDto getDetail(Long bno);

    /**
     * 게시물 목록 조회
     *
     * @param keyword  검색 키워드
     * @param category 카테고리 ID
     * @param pageable 페이징 정보
     * @return 페이징 처리된 게시물 데이터
     */
    Page<BoardDto> getList(String keyword, Long category, Pageable pageable);

    /**
     * 사용자 ID를 통해 회원 ID 조회
     *
     * @param userId 사용자 ID
     * @return 회원 ID
     * @throws IllegalArgumentException 해당 사용자 ID가 없는 경우
     */
    Long getMemberIdByUserId(String userId);

}
