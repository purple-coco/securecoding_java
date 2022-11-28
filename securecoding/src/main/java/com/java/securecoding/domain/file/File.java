package com.java.securecoding.domain.file;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    public static File createFile(String fileName, String filePath) {
        File file = new File();

        file.setFileName(fileName);
        file.setFilePath(filePath);

        return file;
    }
}

