package com.java.securecoding.domain.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileForm {

    private Long id;

    private String fileName;

    private String filePath;
}
