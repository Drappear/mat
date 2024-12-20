package com.example.mat.service;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.won.BoardDto;
import com.example.mat.entity.won.Board;
import com.example.mat.entity.won.BoardImage;
import com.example.mat.repository.BoardImageRepository;
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

    private final BoardRepository boardRepository; // 게시물 저장소
    private final BoardImageRepository boardImageRepository; // 이미지 저장소

    /**
     * 게시물 등록 (이미지 포함되지 않은 경우)
     *
     * @param boardDto 게시물 정보를 담고 있는 DTO
     * @return 등록된 게시물 ID
     */
    @Override
    public Long register(BoardDto boardDto) {
        return registerWithImage(boardDto, null);
    }

    /**
     * 게시물 등록 (이미지 포함)
     *
     * @param boardDto 게시물 정보를 담고 있는 DTO
     * @param image    게시물에 업로드할 이미지 정보
     * @return 등록된 게시물 ID
     */
    @Override
    public Long registerWithImage(BoardDto boardDto, BoardImage image) {

        // 데이터 저장 여부를 확인하는 로그
        System.out.println("Saving board: " + boardDto);
        if (image != null) {
            System.out.println("Saving image: " + image.getImgName());
        }

        Board board = dtoToEntity(boardDto); // DTO -> 엔티티 변환
        boardRepository.save(board); // 게시물 저장

        if (image != null) {
            image.setBoard(board); // 이미지에 게시물 정보 설정
            boardImageRepository.save(image); // 이미지 저장

            System.out.println("Image saved: " + image);
        }

        return board.getBno();
    }

    /**
     * 게시물 수정 (이미지 포함되지 않은 경우)
     *
     * @param boardDto 수정할 게시물 정보를 담고 있는 DTO
     * @return 수정된 게시물 ID
     */
    @Override
    public Long modify(BoardDto boardDto) {
        return modifyWithImage(boardDto, null);
    }

    /**
     * 게시물 수정 (이미지 포함)
     *
     * @param boardDto 수정할 게시물 정보를 담고 있는 DTO
     * @param image    게시물에 업로드할 새로운 이미지 정보
     * @return 수정된 게시물 ID
     */
    @Override
    public Long modifyWithImage(BoardDto boardDto, BoardImage image) {
        Optional<Board> result = boardRepository.findById(boardDto.getBno());
        if (result.isPresent()) {
            Board board = result.get();
            board.setTitle(boardDto.getTitle()); // 제목 수정
            board.setContent(boardDto.getContent()); // 내용 수정

            if (image != null) {
                boardImageRepository.deleteByBoard(board); // 기존 이미지 삭제
                image.setBoard(board); // 새로운 이미지에 게시물 정보 설정
                boardImageRepository.save(image); // 새로운 이미지 저장
                System.out.println("Image updated: " + image);
            }

            boardRepository.save(board); // 수정된 게시물 저장
            System.out.println("Board updated: " + board);

            return board.getBno();
        }
        throw new IllegalArgumentException("게시물이 존재하지 않습니다.");
    }

    /**
     * 게시물 삭제
     *
     * @param bno 삭제할 게시물의 ID
     */
    @Override
    public void delete(Long bno) {
        Optional<Board> result = boardRepository.findById(bno);
        if (result.isPresent()) {
            Board board = result.get();
            boardImageRepository.deleteByBoard(board); // 연관된 이미지 삭제
            boardRepository.deleteById(bno); // 게시글 삭제
        } else {
            throw new IllegalArgumentException("게시물이 존재하지 않습니다.");
        }
    }

    /**
     * 게시물 목록 조회 (페이징 및 검색)
     *
     * @param pageRequestDto 페이징 요청 정보를 담고 있는 DTO
     * @return 페이징 처리된 게시물 결과 DTO
     */
    @Override
    public PageResultDto<BoardDto, Object[]> getList(PageRequestDto pageRequestDto) {
        Function<Object[], BoardDto> fn = (arr -> entityToDto(
                (Board) arr[0],
                null,
                (Long) arr[2]));
        return new PageResultDto<>(boardRepository.getListPage(pageRequestDto.getPageable()), fn);
    }

    /**
     * 게시물 상세 조회
     *
     * @param bno 조회할 게시물의 ID
     * @return 게시물 상세 정보 DTO
     */
    @Override
    public BoardDto getDetail(Long bno) {
        Optional<Board> result = boardRepository.findById(bno);
        if (result.isPresent()) {
            Board board = result.get();
            BoardImage image = board.getImage(); // 연관된 이미지 가져오기
            return entityToDto(board, image, board.getViewCount());
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
                .nickname(dto.getNickname())
                .boardCategory(dto.getBoardCategory())
                .viewCount(0L)
                .build();
    }

    /**
     * Board 엔티티 -> BoardDto 변환
     *
     * @param board     게시물 엔티티
     * @param image     이미지 엔티티
     * @param viewCount 조회수
     * @return 게시물 DTO
     */
    private BoardDto entityToDto(Board board, BoardImage image, Long viewCount) {
        return BoardDto.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .nickname(board.getNickname())
                .boardCategory(board.getBoardCategory())
                .viewCount(viewCount)
                .regDate(board.getRegDate())
                .updateDate(board.getUpdateDate())
                .imageFileName(image != null ? image.getImgName() : null) // 이미지 파일 이름 추가
                .build();
    }
}
