package com.example.mat.service;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.won.BoardDto;
import com.example.mat.entity.won.BoardImage;

/**
 * 게시판 서비스 인터페이스
 * 비즈니스 로직의 계약을 정의합니다.
 */
public interface BoardService {

    /**
     * 게시물 등록
     *
     * @param boardDto 게시물 정보를 담고 있는 DTO
     * @return 등록된 게시물 ID
     */
    Long register(BoardDto boardDto);

    /**
     * 게시물 등록 (이미지 포함)
     *
     * @param boardDto 게시물 정보를 담고 있는 DTO
     * @param image    게시물에 업로드할 이미지 정보
     * @return 등록된 게시물 ID
     */
    Long registerWithImage(BoardDto boardDto, BoardImage image);

    /**
     * 게시물 수정
     *
     * @param boardDto 수정할 게시물 정보를 담고 있는 DTO
     * @return 수정된 게시물 ID
     */
    Long modify(BoardDto boardDto);

    /**
     * 게시물 수정 (이미지 포함)
     *
     * @param boardDto 수정할 게시물 정보를 담고 있는 DTO
     * @param image    게시물에 업로드할 새로운 이미지 정보
     * @return 수정된 게시물 ID
     */
    Long modifyWithImage(BoardDto boardDto, BoardImage image);

    /**
     * 게시물 삭제
     *
     * @param bno 삭제할 게시물의 ID
     */
    void delete(Long bno);

    /**
     * 게시물 목록 조회 (페이징 및 검색)
     *
     * @param pageRequestDto 페이징 요청 정보를 담고 있는 DTO
     * @return 페이징 처리된 게시물 결과 DTO
     */
    PageResultDto<BoardDto, Object[]> getList(PageRequestDto pageRequestDto);

    /**
     * 게시물 상세 조회
     *
     * @param bno 조회할 게시물의 ID
     * @return 게시물 상세 정보 DTO
     */
    BoardDto getDetail(Long bno);
}
