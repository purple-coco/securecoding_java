package com.java.securecoding.repository;

import com.java.securecoding.domain.file.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
}
