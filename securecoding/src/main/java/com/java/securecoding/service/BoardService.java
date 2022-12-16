package com.java.securecoding.service;

import com.java.securecoding.domain.board.Board;
import com.java.securecoding.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    @Value(("${file.dir"))
    private String fileDir;

    private final BoardRepository boardRepository;

    @Transactional
    public Long saveBoard(Board board, MultipartFile file) {
        String originalFilename = "";
        String filePath = "";

        if(!file.isEmpty()) {
            originalFilename = file.getOriginalFilename();
            filePath = fileDir + originalFilename;
        }

        board.setFileName(originalFilename);
        board.setFilePath(filePath);

        boardRepository.save(board);

        return board.getId();
    }

}
