package com.example.mat.service;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.won.BoardDto;
import com.example.mat.entity.won.Board;
import com.example.mat.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

/**
 * 게시판 서비스 구현체
 * 비즈니스 로직을 구현합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    public Long register(BoardDto boardDto) {
        // BoardDto -> Board 엔티티로 변환
        Board board = dtoToEntity(boardDto);
        // 저장 후 저장된 엔티티의 ID 반환
        boardRepository.save(board);
        return board.getBno();
    }

    @Override
    public Long modify(BoardDto boardDto) {
        // 수정할 게시물이 존재하는지 확인
        Optional<Board> result = boardRepository.findById(boardDto.getBno());
        if (result.isPresent()) {
            Board board = result.get();
            board.setTitle(boardDto.getTitle());
            board.setContent(boardDto.getContent());
            return board.getBno();
        }
        throw new IllegalArgumentException("게시물이 존재하지 않습니다.");
    }

    @Override
    public void delete(Long bno) {
        // 게시물 삭제
        if (boardRepository.existsById(bno)) {
            boardRepository.deleteById(bno);
        } else {
            throw new IllegalArgumentException("게시물이 존재하지 않습니다.");
        }
    }

    @Override
    public PageResultDto<BoardDto, Object[]> getList(PageRequestDto pageRequestDto) {
        // 페이지 요청 처리
        Function<Object[], BoardDto> fn = (arr -> entityToDto(
                (Board) arr[0],
                (String) arr[1],
                (Long) arr[2]));
        return new PageResultDto<>(boardRepository.getListPage(pageRequestDto.getPageable()), fn);
    }

    @Override
    public BoardDto getDetail(Long bno) {
        // 게시물 상세 정보 조회
        Optional<Board> result = boardRepository.findById(bno);
        if (result.isPresent()) {
            Board board = result.get();
            return entityToDto(board, null, board.getViewCount());
        }
        throw new IllegalArgumentException("게시물이 존재하지 않습니다.");
    }

    /**
     * BoardDto -> Board 엔티티 변환
     *
     * @param dto 게시물 DTO
     * @return 게시물 엔티티
     */
    private Board dtoToEntity(BoardDto dto) {
        return Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .viewCount(0L)
                .build();
    }

    /**
     * Board 엔티티 -> BoardDto 변환
     *
     * @param board     게시물 엔티티
     * @param nickname  작성자 닉네임
     * @param viewCount 조회수
     * @return 게시물 DTO
     */
    private BoardDto entityToDto(Board board, String nickname, Long viewCount) {
        return BoardDto.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .nickname(nickname)
                .viewCount(viewCount)
                .regDate(board.getRegDate())
                .updateDate(board.getUpdateDate())
                .build();
    }
}
