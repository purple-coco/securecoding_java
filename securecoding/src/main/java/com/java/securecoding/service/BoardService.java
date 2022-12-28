package com.java.securecoding.service;

import com.java.securecoding.domain.board.Board;
import com.java.securecoding.domain.member.Member;
import com.java.securecoding.repository.BoardRepository;
import exception.BoardException;
import exception.NotAllowExtException1;
import exception.NotAllowExtException2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    @Value(("${file.dir"))
    private String fileDir;

    /* 게시판 목록 */
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    /* 게시판 조회 */
    @Transactional
    public Board findBoard(Long boardId) { return boardRepository.findAllById(boardId);}

    @Transactional
    public Long saveBoard(Board board, MultipartFile file) throws IOException {
        String originalFilename = "";
        String filePath = "";

        if (file.isEmpty()) {
            board.setFileName(originalFilename);
            board.setFilePath(filePath);

        } else {
            originalFilename = file.getOriginalFilename();
            filePath = fileDir + originalFilename;

            file.transferTo(new File(filePath));
        }

        boardRepository.save(board);

        return board.getId();
    }

    @Transactional
    public Long saveBoard_secure(Board board, MultipartFile file) throws IOException {
        String originalFilename = " ";
        String filePath = " ";


        if (file.isEmpty()) {
            board.setFileName(originalFilename);
            board.setFilePath(filePath);
        } else {
            String originalFileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);

            if (file.getOriginalFilename().matches("[\\.\\%\\;\\/\\/]*")) {
                throw new NotAllowExtException1("파일 이름에 허용하지 않는 문자열이 포함되었습니다.");
            }
            if (originalFileExt.equals("jpg") || originalFileExt.equals("png")
                    || originalFileExt.equals("docx") || originalFileExt.equals("jpeg")) {

                originalFilename = file.getOriginalFilename();
                filePath = fileDir + originalFilename;

                file.transferTo(new File(filePath));
            } else {
                throw new NotAllowExtException2("업로드 허용되는 파일 확장자가 아닙니다.");
            }
        }
        boardRepository.save(board);

        return board.getId();
    }

    /* 게시물 수정, 삭제 권한 검증 */
    public void validateUpdate(Long memberId, Long boardId) {
        Board findItem = boardRepository.findAllById(boardId);

        if(!findItem.getMember().getId().equals(memberId)) {
            throw new BoardException("자신이 작성한 게시물만 수정 및 삭제가 가능합니다.");
        }

    }

    /* 게시물 수정 */
    @Transactional
    public void updateBoard(Long boardId, String subject, String content, String fileName, String filePath) {
        Board findItem = boardRepository.findAllById(boardId);

        findItem.change(subject, content, fileName, filePath);
    }


    /* 게시물 삭제 */
    @Transactional
    public void deleteBoard(Long id) {

        boardRepository.deleteById(id);

    }

}
