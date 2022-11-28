package com.java.securecoding.service;

import com.java.securecoding.domain.file.File;
import com.java.securecoding.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${file.dir}")
    private String fileDir;

    private final FileRepository fileRepository;

    @Transactional
    public Long saveFile(File file, MultipartFile multipartFile) throws IOException {

        String originalFilename = "";
        String filePath = "";

        if(!multipartFile.isEmpty()) {
            originalFilename = multipartFile.getOriginalFilename();
            filePath = fileDir + originalFilename;

            multipartFile.transferTo(new java.io.File(filePath));
            log.info("파일 저장 {}", filePath);
            log.info("저장 완료");
        }

        file.setFileName(originalFilename);
        file.setFilePath(filePath);

        fileRepository.save(file);

        return file.getId();
    }
}
